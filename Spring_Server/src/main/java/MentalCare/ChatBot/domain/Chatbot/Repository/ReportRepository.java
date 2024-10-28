package MentalCare.ChatBot.domain.Chatbot.Repository;

import MentalCare.ChatBot.domain.Chatbot.Entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
