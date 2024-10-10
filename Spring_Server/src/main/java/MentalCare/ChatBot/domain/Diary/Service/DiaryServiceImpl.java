package MentalCare.ChatBot.domain.Diary.Service;

import MentalCare.ChatBot.domain.Diary.DTO.Request.DiaryRequest;
import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Diary.Repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.image.ImageClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final ChatClient chatClient; //일기 요약 + 채팅 전용 객체
    private final ImageClient imageClient; //일기를 통해 4칸 만화 생성 객체

    @Override
    public String SummarizeDiary(DiaryRequest diaryRequest) {
        /*DTO.toStirng()후 AI연산 시행 결과값을 String type으로 return*/
        return chatClient.call(diaryRequest.toString());
    }

    // TODO : 4컷 만화 생성 후 어떻게 저장하고 전송할지 확인
    @Override
    public String DrawComic(DiaryRequest diaryRequest) {
        return "";
    }

    @Override
    public String SaveDiary(DiaryRequest diaryRequest) {
        /*실제 저장은 하지 않고, String type으로 변환후 return*/
        return diaryRequest.toString();
    }

    @Override
    public String ClassifyEmotion(DiaryRequest diaryRequest) {
        return "";
    }

    @Override
    public Map<String, String> WeatherMatch(String diaryEmotion) {

        String weather;
        String weatherEmoji;

        switch (diaryEmotion) {
            case "기쁨" -> {weather = "Sunny";weatherEmoji = "☀️";}
            case "슬픔" -> {weather = "Rainy";weatherEmoji = "🌧️";}
            case "분노" -> {weather = "Stormy";weatherEmoji = "🌩️";}
            case "평온" -> {weather = "Cloudy";weatherEmoji = "☁️";}
            case "불안" -> {weather = "Windy";weatherEmoji = "🌬️";}
            default -> {weather = "Unknown";weatherEmoji = "❓";}
        }
        return Map.of(weather,weatherEmoji);
    }

    @Override
    public void saveDiary(Diary diary) {
        diaryRepository.save(diary);
    }

    //날짜로 일기 조회 메서드
    @Override
    public Diary getDiaryByDate(LocalDate date) {
        return diaryRepository.findByDiaryDate(date);
    }
}
