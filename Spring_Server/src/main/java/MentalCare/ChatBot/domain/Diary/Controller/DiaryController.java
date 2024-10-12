package MentalCare.ChatBot.domain.Diary.Controller;

import MentalCare.ChatBot.domain.Diary.DTO.Request.DiaryRequest;
import MentalCare.ChatBot.domain.Diary.DTO.Request.PromptRequest;
import MentalCare.ChatBot.domain.Diary.DTO.Response.PromptResponse;
import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Diary.Service.DiaryService;
import MentalCare.ChatBot.domain.FastAPIConnection.Client.ApiClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Map;

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



    @Operation(summary = " 일기 저장 버튼 클릭 API ", description = " 일기 저장 버튼 클릭 시 일기 요약 + 4컷 카툰 제작 + 일기 저장 + 감정 분류 + 분류된 감정을 바탕으로 날씨 이름과 날씨 이모티콘 매칭 후 일기 본문과 만화를 반환하는 API")
    @PostMapping("/diary")
    public ResponseEntity<LinkedHashMap<String, String>> DiarySave(@RequestBody String text) {

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

        Diary diary = new Diary(diaryText, diarySummary, comicURL, diaryEmotion, weather, weatherEmoji, LocalDate.now());
        diaryService.saveDiary(diary); //implementation completed

        return ResponseEntity.ok().body(new LinkedHashMap<String, String>() {{
            put("diaryText", diaryText);
            put("comicURL", comicURL);
        }});

    }

    @Operation(summary = " 일기 조회 API ", description = " 클라이언트 측에서 넘어오는 날자 값을 받아 당일날 일기 본문과 4컷 만화를 반환 ")
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

    /////////////////////
    //                 //
    // 아래는 TEST 코드  //
    //                 //
    /////////////////////

    // TODO : DTO을 받아서 ai 연산을 수행할 것인가? 아니면 String 값을 받아서 ai 연산을 수행할 것인가?
    @PostMapping("/diary/sumarize")
    public  String sumarizetest(@RequestBody String message){
        String prompt = "다음 일기를 3줄 또는 4줄로 요약해 주세요.";
        String fullMessage = prompt + message;
        return chatClient.call(fullMessage);
    }

    //TODO : 리펙토링 요망
    @PostMapping("/diary/generate")
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
    public String fastapi(@RequestBody String text ){
        String prompt ="이 일기를 (기쁨,슬픔, 평안, 분노, 불안) 중 하나의 감정으로 감정을 분류해줘. 대답할때는 대답할때는 기쁨,슬픔 이렇게 단어로 답변을 해줘.-> ";
        String fullmessage = prompt + text;
        return apiClient.sendData(fullmessage);
    }

}
