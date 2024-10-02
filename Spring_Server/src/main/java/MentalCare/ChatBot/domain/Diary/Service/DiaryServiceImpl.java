package MentalCare.ChatBot.domain.Diary.Service;

import MentalCare.ChatBot.domain.Diary.DTO.Request.DiaryRequest;
import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Diary.Repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;

    /*요약하고 fast-api 로 데이터를 넘겨야 한다*/
    //DB에 저장할 필요는 없음
    @Override
    public String SummarizeDiary(DiaryRequest diaryRequest) {
        return "";
    }

    @Override
    public String DrawComic(DiaryRequest diaryRequest) {
        return "";
    }

    @Override
    public String SaveDiary(DiaryRequest diaryRequest) {
        return "";
    }

    @Override
    public String ClassifyEmotion(DiaryRequest diaryRequest) {
        return "";
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
