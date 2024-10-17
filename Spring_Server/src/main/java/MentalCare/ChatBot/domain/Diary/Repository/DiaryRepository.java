package MentalCare.ChatBot.domain.Diary.Repository;

import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findByMember(Member member);//멤버 찾기
    Diary findByDiaryDate(LocalDate date); // 날짜로 일기 조회
    Optional<Diary> findByMemberAndDiaryDate(Member member, LocalDate diaryDate);// 특정 날짜와 사용자에 해당하는 일기를 찾는 메서드

    List<Diary> findByDiaryEmotion(String diaryEmotion);
    List<Diary> findByDiarySummaryContaining(String summary);
}
