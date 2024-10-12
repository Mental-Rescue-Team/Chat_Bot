package MentalCare.ChatBot.domain.Diary.Service;

import MentalCare.ChatBot.domain.Diary.Entity.Diary;

import java.time.LocalDate;
import java.util.Map;

public interface DiaryService {

    /*일기 요약*/
    String SummarizeDiary(String text);

    /*일기 기반 4칸 만화 생성*/
    String DrawComic(String text);

    /*저장 메서드  */
    String SaveDiary(String text);

    /*일기 감성 분류 메서드*/
    String ClassifyEmotion(String text);

    /*감성 - <일기, 이모티콘> 매칭 메서드*/
    Map<String,String> WeatherMatch( String diaryEmotion);

    /*diary 객체 저장 메서드*/
    void saveDiary(Diary diary);

    /*날짜 기반 일기 조회 메서드*/
    Diary getDiaryByDate(LocalDate date);
}
