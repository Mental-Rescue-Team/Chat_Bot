package MentalCare.ChatBot.domain.Chatbot.Repository;

import MentalCare.ChatBot.domain.Chatbot.DTO.Response.ReportDTO;
import MentalCare.ChatBot.domain.Chatbot.Entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    /* 지금껏 오류가 났던 이유는 올바르지 못한 @param을 임포트 했기 때문이다. */

    @Query("SELECT new MentalCare.ChatBot.domain.Chatbot.DTO.Response.ReportDTO(r.report_no, r.currentDifficulty, r.currentEmotion, r.aiAdvice) " +
            "FROM Report r WHERE r.member.member_no = :memberNo")
    List<ReportDTO> findReportDTOByMemberNo(@Param("memberNo") Long memberNo);


}

