package MentalCare.ChatBot.domain.Diary.Controller;

import MentalCare.ChatBot.domain.Diary.DTO.Request.DiaryRequest;
import MentalCare.ChatBot.domain.Diary.DTO.Request.PromptRequest;
import MentalCare.ChatBot.domain.Diary.DTO.Response.PromptResponse;
import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Diary.Service.DiaryService;
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
import java.util.Map;

@Tag(name = "Diary", description = "일기 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DiaryController {

    private final DiaryService diaryService;
    private final ChatClient chatClient; //일기 요약 + 채팅 전용 객체
    private final ImageClient imageClient; //일기를 통해 4칸 만화 생성 객체

    @Operation(summary = " 일기 저장 버튼 클릭 API ", description = " 일기 저장 버튼 클릭 시 일기 요약 + 4컷 카툰 제작 + 일기 저장 + 감정 분류 + 분류된 감정을 바탕으로 날씨 이름과 날씨 이모티콘 매칭 후 일기 본문과 만화를 반환하는 API")
    @PostMapping("/diary")
    public ResponseEntity<?> DiarySave(@RequestBody DiaryRequest diaryRequest) {

        String diarySummary = diaryService.SummarizeDiary(diaryRequest);
        String comicURL = diaryService.DrawComic(diaryRequest);
        String diaryText = diaryService.SaveDiary(diaryRequest);
        String diaryEmotion = diaryService.ClassifyEmotion(diaryRequest);

        Map<String,String> weatherMatch = diaryService.WeatherMatch(diaryEmotion);
        String weather = weatherMatch.get("weather");
        String weatherEmoji = weatherMatch.get("weatherEmoji");

        Diary diary = new Diary(diaryText, diarySummary, comicURL, diaryEmotion, weather, weatherEmoji, LocalDate.now());
        diaryService.saveDiary(diary);

        return ResponseEntity.ok().body(Map.of(
                "diaryText", diaryText,
                "comicURL", comicURL
        ));
    }

    @Operation(summary = " 일기 조회 API ", description = " 쿨라이언트 측에서 넘어오는 날자 값을 받아 당일날 일기 본문과 4컷 만화를 반환 ")
    @GetMapping("/diary")
    public ResponseEntity<?> DiaryShow(@RequestParam("date") String date){

        LocalDate diaryDate = LocalDate.parse(date); // 문자열을 LocalDate로 변환
        Diary diary = diaryService.getDiaryByDate(diaryDate);

        if (diary == null) {return ResponseEntity.notFound().build();}

        Map<String, Object> response = Map.of(
                "diaryText", diary.getDiaryText(),
                "comicURL", diary.getComicURL()
        );// 응답 데이터 생성

        return ResponseEntity.ok().body(response);
    }


    // TODO : DTO을 받아서 ai 연산을 수행할 것인가? 아니면 String 값을 받아서 ai 연산을 수행할 것인가?
    @PostMapping("/diary/sumarize")
    public  String sumarizetest(@RequestBody String message){
        //String openAiResponse = chatClient.call(message);
        return chatClient.call(message);
    }

    //TODO : 리펙토링 요망
    @PostMapping("/diary/generate")
    public PromptResponse getImage(@RequestBody PromptRequest request) {
        final String prompt = request.prompt();

        // input validation
        if (StringUtils.isEmpty(prompt)) {
            System.out.println("Prompt is required");
            throw new IllegalArgumentException("Prompt is required");
        }

        System.out.println("Prompt: " + prompt);
        System.out.println("Generating image...");

        // image options
        OpenAiImageOptions imageOptions = OpenAiImageOptions.builder()
                .withQuality("standard")
                .withHeight(1024)
                .withN(1)
                .withWidth(1792)
                .build();

        // image prompt
        ImagePrompt imagePrompt = new ImagePrompt(prompt, imageOptions);

        System.out.println("Calling image client...");
        // class ep
        Image img = imageClient.call(imagePrompt).getResult().getOutput();

        System.out.println("Image generated successfully");
        return new PromptResponse(img.getUrl());
    }
}
