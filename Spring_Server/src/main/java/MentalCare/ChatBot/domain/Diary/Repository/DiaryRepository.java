package MentalCare.ChatBot.domain.Diary.Repository;

import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    //멤버 찾기
    List<Diary> findByMember(Member member);

    //맴버 번호로 다이어리 리스트 가져오기
    /*문제 발생 point 쿼리 인식 불가능 한것 같음*/
    //List<Diary> findByMember_no(Long member_no);

    //감정으로 일기 조회
    List<Diary> findByDiaryEmotion(String diaryEmotion);

    //요약으로 일기 조회
    List<Diary> findByDiarySummaryContaining(String summary);

    //날짜로 일기 조회
    Diary findByDiaryDate(LocalDate date); // 날짜로 일기 조회 메서드 추가

    //List<Diary> getDiariesByMonthAndUser(int month,Long member_no);

}
