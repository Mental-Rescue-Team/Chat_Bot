package MentalCare.ChatBot.domain.Diary.Controller;

import MentalCare.ChatBot.domain.Diary.DTO.Request.DiaryRequest;
import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Diary.Service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "Diary", description = "일기 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DiaryController {

    //아래 서비스는 인터페이스를 구현한 구현채까지 만들고 서비스 어노테이션까지 붙혀줘야 오류가 안남
    private final DiaryService diaryService;
    private final OpenAiChatModel openAiChatModel;
    //private final ImageModel imageModel;

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

        // TODO : 엔티티에 set를 적용하고 있다. 추후 변동할것
        // Diary 엔티티 생성 및 필드 설정
        Diary diary = new Diary();
        diary.setDiaryText(diaryText);
        diary.setDiarySummary(diarySummary);
        diary.setComicURL(comicURL);
        diary.setDiaryEmotion(diaryEmotion);
        diary.setWeather(weather);
        diary.setWeatherEmoji(weatherEmoji);
        diary.setDiaryDate(LocalDate.now()); // 오늘 날짜로 설정

        diaryService.saveDiary(diary);

        return ResponseEntity.ok().body(Map.of(
                "diaryText", diaryText,
                "comicURL", comicURL
        ));
    }

    //클라이언트가 선택한 날짜를 기반으로 한 일기 조회 메서드
    //텍스트와 만화 URL만 반환
    @Operation(summary = " 일기 조회 API ", description = " 쿨라이언트 측에서 넘어오는 날자 값을 받아 당일날 일기 본문과 4컷 만화를 반환 ")
    @GetMapping("/diary")
    public ResponseEntity<?> DiaryShow(@RequestParam("date") String date){

        LocalDate diaryDate = LocalDate.parse(date); // 문자열을 LocalDate로 변환
        Diary diary = diaryService.getDiaryByDate(diaryDate);

        if (diary == null) {
            return ResponseEntity.notFound().build(); // 일기가 없으면 404 Not Found 반환}
        }

        // 응답 데이터 생성
        Map<String, Object> response = Map.of(
                "diaryText", diary.getDiaryText(),
                "comicURL", diary.getComicURL()
        );

        return ResponseEntity.ok().body(response);
    }

    /*일기 요약 테스팅 용 API*/
    //테스팅 성공 - 4줄요약으로 진행
    //요청 답변 시간 - 2.04s
    //Prompting 메시지 추가 요망
    // TODO : DTO을 받아서 ai 연산을 수행할 것인가? 아니면 String 값을 받아서 ai 연산을 수행할 것인가?
    @PostMapping("/diary/sumarize/test")
    public  Map<String, String> sumarizetest(@RequestBody String message){
        Map<String, String> responses = new HashMap<>();

        String openAiResponse = openAiChatModel.call(message);
        responses.put("openai(chatGPT) 응답", openAiResponse);

        return responses;

          /*아래 코드는 받은 DTO를 string으로 변환 한 후 사용 하는 것이다.*/
//        Map<String, String> responses = new HashMap<>();
//        String diaryRequestString = diaryRequest.toString();
//
//        String openAiResponse = openAiChatModel.call(diaryRequestString);
//        responses.put("openai(chatGPT) 응답", openAiResponse);
    }
}
