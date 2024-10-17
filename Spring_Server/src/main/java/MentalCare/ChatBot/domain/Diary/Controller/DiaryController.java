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
    private final ChatClient chatClient; //일기 요약 + 채팅 전용 객체
    private final ImageClient imageClient; //일기를 통해 4칸 만화 생성 객체
    private final ApiClient apiClient;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;

    @Operation(summary = " 일기 저장 버튼 클릭 API ", description = " 일기 저장 버튼 클릭 시 일기 요약 + 4컷 카툰 제작 + 일기 저장 + 감정 분류 + 분류된 감정을 바탕으로 날씨 이름과 날씨 이모티콘 매칭 후 일기 본문과 만화를 반환하는 API")
    @PostMapping("/diary")
    public ResponseEntity<LinkedHashMap<String, String>> DiarySave(@RequestBody String text, HttpServletRequest request) {

        String diarySummary = diaryService.SummarizeDiary(text); //implementation completed
        String comicURL = diaryService.DrawComic(text); //implementation completed
        String diaryText = diaryService.SaveDiary(text); //implementation completed
        String diaryEmotion = diaryService.ClassifyEmotion(text); //implementation completed - replace it with GPT at Fast-API for now.
        System.out.println("컨트롤러 단 diaryEmotion :" + diaryEmotion);

        Map<String,String> weatherMatch = diaryService.WeatherMatch(diaryEmotion); //implementation completed
        String weather = weatherMatch.get("weather");
        String weatherEmoji = weatherMatch.get("weatherEmoji");
        System.out.println("클라이언트 측 :"+weather);
        System.out.println("클라이언트 측 :" + weatherEmoji);

        String userToken =jwtUtil.extractTokenFromRequest(request);
        if (userToken == null || userToken.isEmpty())
        {throw new MemberException(ErrorCode.TOKEN_MUST_FILLED);}

        String username = jwtUtil.extractUsername(userToken);
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        Diary diary = new Diary(member,diaryText, diarySummary, comicURL, diaryEmotion, weather, weatherEmoji, LocalDate.now());
        diaryService.saveDiary(diary); //implementation completed

        return ResponseEntity.ok().body(new LinkedHashMap<String, String>() {{
            put("diaryText", diaryText);
            put("comicURL", comicURL);
        }});
    }

    @Operation(summary = " 일기 조회 API ", description = " 클라이언트 측에서 넘어오는 날짜 값을 받아 당일날 일기 본문과 4컷 만화를 반환 ")
    @GetMapping("/diary")
    public ResponseEntity<Map<String, String>> DiaryShow(@RequestParam("date") String date){

        LocalDate diaryDate;
        try {
            diaryDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date format"));
        }

        Diary diary = diaryService.getDiaryByDate(diaryDate);
        if (diary == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> response = Map.of(
                "diaryText", diary.getDiaryText(),
                "comicURL", diary.getComicURL()
        );// 응답 데이터 생성
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "오늘의 날씨 조회 API ", description = "오늘 날짜를 받아서, 오늘의 날씨 이름을 클라이언트 측으로 전송 ")
    @GetMapping("/today/weather")
    public ResponseEntity<String> todayWeather(){

        LocalDate diaryDate = LocalDate.now();
        System.out.println(diaryDate);
        Diary diary = diaryRepository.findByDiaryDate(diaryDate);
        String todayWeather = diary.getWeather();

        return ResponseEntity.ok().body(todayWeather);
    }

    //TODO : Refactor - 컨트롤러 서비스 영역 분리하기
    @Operation(summary = "월별 날씨 이모티콘 모두 조회 API ", description = "오늘 날짜를 받아서, 오늘의 날씨 이름을 클라이언트 측으로 전송 ")
    @GetMapping("/every/weathers")
    //월 값을 받아야 함 / Get 요청이 가장 응답이 빠르기 때문에 GET 요청으로 메인페이지를 로딩한다.
    public ResponseEntity<List<DateEmoji>> loadEmojis(@RequestParam("month") int month, HttpServletRequest request){

        //토큰 추출
        String userToken =jwtUtil.extractTokenFromRequest(request);
        System.out.println(userToken);
        if (userToken == null || userToken.isEmpty()) {
            throw new MemberException(ErrorCode.TOKEN_MUST_FILLED);
        }

        //토큰에서 사용자 이름 추출
        String username = jwtUtil.extractUsername(userToken);
        System.out.println(username);

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        // 사용자로 다이어리 리스트 추출
        List<Diary> diaries = diaryRepository.findByMember(member);

        // 해당 월의 날씨 이모티콘과 날짜 추출
        List<DateEmoji> dateEmojis = diaries.stream()
                .filter(diary -> diary.getDiaryDate().getMonthValue() == month) // 특정 월 필터링
                .map(diary -> new DateEmoji(diary.getDiaryDate(), diary.getWeatherEmoji())) // DateEmoji로 변환
                .collect(Collectors.toList());

        // 결과 반환
        return ResponseEntity.ok(dateEmojis);

    }

    //TODO : Refactor - 컨트롤러, 서비스 분리
    @Operation(summary = "사용자 감정에 따른 채팅 배경화면 전환 API ", description = "사용자 감정을 클라이언트 측에 보내니, 클라이언트츨에서 감정에 맞는 배경화면을 매칭시킬 것")
    @GetMapping("/diary/emotion")
    public ResponseEntity<String> getMember_emotion(HttpServletRequest request){

        /* 요청값에서 토큰 추출 */
        String userToken =jwtUtil.extractTokenFromRequest(request);
        System.out.println(userToken);
        if (userToken == null || userToken.isEmpty()) {
            throw new MemberException(ErrorCode.TOKEN_MUST_FILLED);
        }

        /* 토큰에서 사용자 이름 추출 */
        String username = jwtUtil.extractUsername(userToken);

        /* 사용자 이름으로 사용자 추출 */
        Member member =  memberRepository.findByUsername(username)
                .orElseThrow(()-> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        /* 사용자로 다이어리 엔티티에서 오늘의 감정을 추출 후 리턴  */
        LocalDate diaryDate = LocalDate.now();
        String diaryEmotion = diaryRepository.findByMemberAndDiaryDate(member,diaryDate)
                    .map(Diary::getDiaryEmotion)
                    .orElseThrow(() -> new DiaryException(ErrorCode.DIARY_NOT_FOUND_FOR_DATE));

        return ResponseEntity.ok(diaryEmotion);
    }

    /* Test Method Below */

    // TODO : DTO을 받아서 ai 연산을 수행할 것인가? 아니면 String 값을 받아서 ai 연산을 수행할 것인가?
    @PostMapping("/diary/summary")
    public  String sumarizeTest(@RequestBody String message){
        String prompt = "다음 일기를 3줄 또는 4줄로 요약해 주세요.";
        String fullMessage = prompt + message;
        return chatClient.call(fullMessage);
    }

    //TODO : 리펙토링 요망
    @PostMapping("/diary/comic")
    public PromptResponse getImage(@RequestBody PromptRequest request) {

        final String prompt = request.prompt();

        if (StringUtils.isEmpty(prompt)) {
            System.out.println("Prompt is required");
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

    /* 아래 두가지로 테스팅 해보기 */
    /* 테스팅 시나리오 */

    //1.아이디로 멤버 찾아주는 테스팅 메서드
    @PostMapping("/member/test")
    public Member findById(@RequestParam("member_no")  Long member_no){
        return memberRepository.findById(member_no)
            .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
    }

    //2.아이디로 다이어리 리스트 찾아주는 테스팅 메서드
    @PostMapping("/diaries/test")
    public List<Diary> findByMember_no(@RequestBody Member member){
        List<Diary> diaries = diaryRepository.findByMember(member);
        for (Diary diary : diaries) {
            System.out.println(diary);
        }
        return diaries;
    }
}
