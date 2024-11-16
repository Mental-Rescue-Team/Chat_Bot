package MentalCare.ChatBot.domain.Diary.Service;

import MentalCare.ChatBot.domain.Diary.DTO.Response.DateEmoji;
import MentalCare.ChatBot.domain.Diary.DTO.Response.ImageResult;
import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Member.Entity.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DiaryService {

    /*일기 요약*/
    String SummarizeDiary(String text);

    /*일기 기반 4칸 만화 생성*/
    ImageResult DrawComic(String text , String gender);

    /*저장 메서드  */
    String SaveDiary(String text);

    /*일기 감성 분류 메서드*/
    String ClassifyEmotion(String text);

    /*감성 - <일기, 이모티콘> 매칭 메서드*/
    Map<String,String> WeatherMatch( String diaryEmotion);

    /*diary 객체 저장 메서드*/
    void saveDiary(Diary diary);

    /*날짜 기반 일기 조회 메서드*/
    Diary getDiaryByDate(LocalDate date,Long member_no);

    /*월별 (날짜/날씨 이모티콘) 모두 전송 메서드*/
    List<DateEmoji> getEveryDateEmoji(int month, Member member);

    /*요청으로 부터사용자 감정 추출 메서드*/
    String getMemberEmotion(Member member);

    /*감정 별 카운트 메서드*/
    Map<String, Long> getEmotionCounts();
}
