package MentalCare.ChatBot.domain.Diary.Controller;

import MentalCare.ChatBot.domain.Diary.DTO.Request.PromptRequest;
import MentalCare.ChatBot.domain.Diary.DTO.Response.DateEmoji;
import MentalCare.ChatBot.domain.Diary.DTO.Response.PromptResponse;
import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Diary.Repository.DiaryRepository;
import MentalCare.ChatBot.domain.Diary.Service.DiaryService;
import MentalCare.ChatBot.domain.FastAPIConnection.Client.ApiClient;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.Exception.DiaryException;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "Diary", description = "일기 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8000")
public class DiaryController {

    private final DiaryService diaryService;
    private final ChatClient chatClient;
    private final ImageClient imageClient;
    private final ApiClient apiClient;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;

    @Operation(summary = " 일기 저장 버튼", description = " 일기 저장 버튼 클릭 시 일기 요약 + 4컷 카툰 제작 + 일기 저장 + 감정 분류 + 분류된 감정을 바탕으로 날씨 이름과 날씨 이모티콘 매칭 후 일기 본문과 만화를 반환하는 API")
    @PostMapping("/diary")
    public ResponseEntity<LinkedHashMap<String, String>> DiarySave(@RequestBody String text, HttpServletRequest request) {

        String diarySummary = diaryService.SummarizeDiary(text);
        String comicURL = diaryService.DrawComic(text);
        String diaryText = diaryService.SaveDiary(text);
        String diaryEmotion = diaryService.ClassifyEmotion(text);

        Map<String,String> weatherMatch = diaryService.WeatherMatch(diaryEmotion);
        String weather = weatherMatch.get("weather");
        String weatherEmoji = weatherMatch.get("weatherEmoji");

        String userToken =jwtUtil.extractTokenFromRequest(request);
        jwtUtil.validateToken_isTokenValid(userToken);

        String username = jwtUtil.extractUsername(userToken);
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        Diary diary = Diary.builder()
                .member(member)
                .diaryText(diaryText)
                .diarySummary(diarySummary)
                .comicURL(comicURL)
                .diaryEmotion(diaryEmotion)
                .weather(weather)
                .weatherEmoji(weatherEmoji)
                .diaryDate(LocalDate.now())
                .build();
        diaryService.saveDiary(diary);

        return ResponseEntity.ok().body(new LinkedHashMap<String, String>() {{
            put("diaryText", diaryText);
            put("comicURL", comicURL);
        }});
    }

    @Operation(summary = " 일기 조회 ", description = " 클라이언트 측에서 넘어오는 날짜 값을 받아 당일날 일기 본문과 4컷 만화를 반환 ")
    @GetMapping("/diary")
    public ResponseEntity<Map<String, String>> DiaryShow(@RequestParam("date") String date){

        LocalDate diaryDate;
        try {
            diaryDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date format"));
        }

        Diary diary = diaryService.getDiaryByDate(diaryDate);
        if (diary == null) {return ResponseEntity.notFound().build();}

        Map<String, String> response = Map.of(
                "diaryText", diary.getDiaryText(),
                "comicURL", diary.getComicURL()
        );// 응답 데이터 생성

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "오늘의 날씨 전송 ", description = "오늘 날짜를 받아서, 오늘의 날씨 이름을 클라이언트 측으로 전송 ")
    @GetMapping("/today/weather")
    public ResponseEntity<String> todayWeather(){

        LocalDate diaryDate = LocalDate.now();
        Diary diary = diaryRepository.findByDiaryDate(diaryDate);
        String todayWeather = diary.getWeather();

        return ResponseEntity.ok().body(todayWeather);
    }

    @Operation(summary = "월별 (날짜/날씨 이모티콘) 모두 전송 ", description = "오늘 날짜를 받아서, 오늘의 날씨 이름을 클라이언트 측으로 전송 ")
    @GetMapping("/every/weathers")
    public ResponseEntity<List<DateEmoji>> loadEmojis(@RequestParam("month") int month, HttpServletRequest request){

        String userToken =jwtUtil.extractTokenFromRequest(request);
        jwtUtil.validateToken_isTokenValid(userToken);
        Member member = memberRepository.findByUsername(jwtUtil.extractUsername(userToken))
                .orElseThrow(()-> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        return ResponseEntity.ok(diaryService.getEveryDateEmoji(month,member));
    }

    @Operation(summary = "사용자 감정에 따른 채팅 배경화면 매칭 ", description = "이 메서드로 사용자 감정을 클라이언트 측에 보내니, 클라이언트츨에서 감정에 맞는 배경화면을 매칭시킬 것")
    @GetMapping("/diary/emotion")
    public ResponseEntity<String> getMember_emotion(HttpServletRequest request){

        String userToken =jwtUtil.extractTokenFromRequest(request);
        jwtUtil.validateToken_isTokenValid(userToken);
        Member member =  memberRepository.findByUsername(jwtUtil.extractUsername(userToken))
                .orElseThrow(()-> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        return ResponseEntity.ok(diaryService.getMemberEmotion(member));
    }

    /* Test Method Below */

    @PostMapping("/diary/summary")
    public  String sumarizeTest(@RequestBody String message){
        String prompt = "다음 일기를 3줄 또는 4줄로 요약해 주세요.";
        String fullMessage = prompt + message;
        return chatClient.call(fullMessage);
    }

    @PostMapping("/diary/comic")
    public PromptResponse getImage(@RequestBody PromptRequest request) {

        final String prompt = request.prompt();

        if (StringUtils.isEmpty(prompt)) {
            throw new IllegalArgumentException("Prompt is required");
        }

        // 이미지 설정
        OpenAiImageOptions imageOptions = OpenAiImageOptions.builder()
                .withQuality("standard")
                .withHeight(1024)
                .withN(1)
                .withWidth(1792)
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(prompt, imageOptions);
        Image img = imageClient.call(imagePrompt).getResult().getOutput();
        return new PromptResponse(img.getUrl());
    }

    /*fast-api 와 통신 성공한 APi */
    // 우선 감정 분류 테스팅도 통과함
    @PostMapping("/fast")
    public String fastAPI(@RequestBody String text ){
        String prompt ="이 일기를 (기쁨,슬픔, 평안, 분노, 불안) 중 하나의 감정으로 감정을 분류해줘. 대답할때는 대답할때는 기쁨,슬픔 이렇게 단어로 답변을 해줘.-> ";
        String fullMessage = prompt + text;
        return apiClient.sendData(fullMessage);
    }

}
