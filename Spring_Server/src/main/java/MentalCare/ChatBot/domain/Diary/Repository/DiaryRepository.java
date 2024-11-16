package MentalCare.ChatBot.domain.Diary.Repository;

import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findByMember(Member member);//멤버 찾기
    Optional<Diary> findByMemberAndDiaryDate(Member member, LocalDate diaryDate);// 특정 날짜와 사용자에 해당하는 일기를 찾는 메서드

    @Query("SELECT d FROM Diary d WHERE d.diaryDate = :date AND d.member.member_no = :member_no")
    Diary findByDiaryDateAndMemberNo(@Param("date") LocalDate date, @Param("member_no") Long member_no);

    @Query("SELECT d.diaryEmotion, COUNT(d) FROM Diary d GROUP BY d.diaryEmotion")
    List<Object[]> countDiariesByEmotion();


}


