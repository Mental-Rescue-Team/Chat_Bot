package MentalCare.ChatBot.domain.Admin.Repository;

import MentalCare.ChatBot.domain.Member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminReopsitory extends JpaRepository<Member,Long> {

}
