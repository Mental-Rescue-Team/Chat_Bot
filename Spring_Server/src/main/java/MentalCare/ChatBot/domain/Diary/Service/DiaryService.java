package MentalCare.ChatBot.domain.Diary.Service;

import MentalCare.ChatBot.domain.Diary.DTO.Request.DiaryRequest;
import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import java.time.LocalDate;
import java.util.Map;

public interface DiaryService {

    String SummarizeDiary(DiaryRequest diaryRequest);

    String DrawComic(DiaryRequest diaryRequest);

    String SaveDiary(DiaryRequest diaryRequest);

    String ClassifyEmotion(DiaryRequest diaryRequest);

    Map<String,String> WeatherMatch( String diaryEmotion);

    void saveDiary(Diary diary);

    Diary getDiaryByDate(LocalDate date);
}
