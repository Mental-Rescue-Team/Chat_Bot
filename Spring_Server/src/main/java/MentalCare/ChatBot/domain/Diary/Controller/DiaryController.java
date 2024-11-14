package MentalCare.ChatBot.domain.Diary.Controller;

import MentalCare.ChatBot.domain.Diary.DTO.Request.PromptRequest;
import MentalCare.ChatBot.domain.Diary.DTO.Response.DateEmoji;
import MentalCare.ChatBot.domain.Diary.DTO.Response.PromptResponse;
import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Diary.Repository.DiaryRepository;
import MentalCare.ChatBot.domain.Diary.Service.DiaryService;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Diary", description = "일기 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
@CrossOrigin(origins = "http://localhost:8000")
public class DiaryController {

    private final DiaryService diaryService;
    private final ChatClient chatClient;
    private final ImageClient imageClient;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;


    @Operation(summary = "일기 저장 버튼 - 왠지 모르게 일기값을 입력할때 , 변수명을 message로 입력을 해야지 오류가 안납니다. text가 아닌 message로 보내주십시요. ", description = " 일기 저장 버튼 클릭 시 일기 요약 + 4컷 카툰 제작 + 일기 저장 + 감정 분류 + 분류된 감정을 바탕으로 날씨 이름과 날씨를 매칭 후 일기 본문과 만화를 반환하는 API")
    @PostMapping("")
    public ResponseEntity<LinkedHashMap<String, String>> DiarySave(@RequestBody String text, HttpServletRequest request) {

        // TODO : 이 부분 블로그에 정리하기

        /* JSON 문자열에서 "message" 필드 값만 추출*/
        String message;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(text);
            message = rootNode.get("message").asText();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LinkedHashMap<String, String>() {{
                put("error", "Invalid JSON format");
            }});
        }
        String userToken =jwtUtil.extractTokenFromRequest(request);                 jwtUtil.validateToken_isTokenValid(userToken);
        String username = jwtUtil.extractUsername(userToken);
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
        String gender = member.getGender(); ;

        String diarySummary = diaryService.SummarizeDiary(message);
        String comicURL = diaryService.DrawComic(message,gender);
        String diaryText = diaryService.SaveDiary(message);
        String diaryEmotion = diaryService.ClassifyEmotion(message);

        Map<String,String> weatherMatch = diaryService.WeatherMatch(diaryEmotion);
        String weather = weatherMatch.get("weather");
        String weatherEmoji = weatherMatch.get("weatherEmoji"); //날씨 이모지는 클라이언트 측에서 준비하기로 함

        Diary diary = Diary.builder()
                .member(member)
                .diaryText(diaryText)
                .diarySummary(diarySummary)
                .comicURL(comicURL)
                .diaryEmotion(diaryEmotion)
                .weather(weather)
                .weatherEmoji("null")
                .diaryDate(LocalDate.now())
                .build();
        diaryService.saveDiary(diary);

        return ResponseEntity.ok().body(new LinkedHashMap<String, String>() {{
            put("diaryText", diaryText);
            put("comicURL", comicURL);
        }});
    }

    @Operation(summary = " 일기 조회 ", description = " 클라이언트 측에서 넘어오는 날짜 값을 받아 당일날 일기 본문과 4컷 만화를 반환gksek ")
    @GetMapping("")
    public ResponseEntity<Map<String, String>> DiaryShow(@RequestParam("date") String date,HttpServletRequest request){

        LocalDate diaryDate;
        try {
            diaryDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date format"));
        }

        String userToken =jwtUtil.extractTokenFromRequest(request);
        String username = jwtUtil.extractUsername(userToken);
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
        Long member_no =member.getMember_no();

        Diary diary = diaryService.getDiaryByDate(diaryDate,member_no);
        if (diary == null) {return ResponseEntity.notFound().build();}

        Map<String, String> response = Map.of(
                "diaryText", diary.getDiaryText(),
                "comicURL", diary.getComicURL()
        );// 응답 데이터 생성

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "메인 페이지 상단에 표시할 오늘의 날씨 전송 ", description = "오늘 날짜를 받아서, 오늘의 날씨 이름을 클라이언트 측으로 전송 - 날씨이름에 맞는 날씨 사진과 메시지는 클라이언트 측에서 준비 ")
    @GetMapping("/today/weather")
    public ResponseEntity<String> todayWeather(HttpServletRequest request){

        String userToken =jwtUtil.extractTokenFromRequest(request);
        String username = jwtUtil.extractUsername(userToken);
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
        Long member_no =member.getMember_no();

        LocalDate diaryDate = LocalDate.now();
        Diary diary = diaryRepository.findByDiaryDateAndMemberNo(diaryDate, member_no);
        String todayWeather = diary.getWeather();

        return ResponseEntity.ok().body(todayWeather);
    }

    @Operation(summary = " 메인 페이지의 캘린더에 날씨 이모티콘을 띄우는 기능", description = " 서버측에서 클라이언트 측으로 (날짜-날씨 이름) 을 보내줄테니 클라이언트 측에서는 이를 받아, 각 날짜에 맞는 날씨 이모티콘을 삽입")
    @GetMapping("/every/weathers")
    public ResponseEntity<List<DateEmoji>> loadEmojis(@RequestParam("month") int month, HttpServletRequest request){

        String userToken =jwtUtil.extractTokenFromRequest(request);
        jwtUtil.validateToken_isTokenValid(userToken);
        Member member = memberRepository.findByUsername(jwtUtil.extractUsername(userToken))
                .orElseThrow(()-> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        return ResponseEntity.ok(diaryService.getEveryDateEmoji(month,member));
    }

    // TODO : 사용자가 일기를 작성하지 않고 , 바로 채팅을 하러 가능 경우를 계산
    @Operation(summary = "사용자가 오늘 작성한 일기의 감정에 따른 채팅 배경화면 매칭 ", description = "이 메서드로 사용자의 일기에서 도출된 감정을 클라이언트 측에 보내니, 클라이언트츨에서 감정에 맞는 채팅 배경화면을 매칭시킬 것, 만약 사용자가 아직 일기를 작성하지 않아 감정이 없는 경우는 [아직 오늘의 감정이 없음]이라는 메시지를 클라이언트에 보냅니다. 그럼 배경화면은 기본배경화면으로 해주면 좋을 것 같습니다")
    @GetMapping("/background")
    public ResponseEntity<String> getMember_emotion(HttpServletRequest request){

        String userToken =jwtUtil.extractTokenFromRequest(request);
        jwtUtil.validateToken_isTokenValid(userToken);
        Member member =  memberRepository.findByUsername(jwtUtil.extractUsername(userToken))
                .orElseThrow(()-> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        return ResponseEntity.ok(diaryService.getMemberEmotion(member));
    }

    /* Test Method Below */

    @Operation(summary = "실제 사용할 api가 아닌 백엔드 테스팅 용 api 입니다. 클라이언트 측에서 연결할 필요 없습니다. ", description = "")
    @PostMapping("/summary")
    public  String sumarizeTest(@RequestBody String message){
        String prompt = "다음 일기를 3줄 또는 4줄로 요약해 주세요.";
        String fullMessage = prompt + message;
        return chatClient.call(fullMessage);
    }
    @Operation(summary = "실제 사용할 api가 아닌 백엔드 테스팅 용 api 입니다. 클라이언트 측에서 연결할 필요 없습니다. ", description = "")
    @PostMapping("/comic")
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

}
