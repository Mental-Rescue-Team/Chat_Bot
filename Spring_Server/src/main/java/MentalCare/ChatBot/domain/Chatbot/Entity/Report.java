package MentalCare.ChatBot.domain.Chatbot.Entity;

import MentalCare.ChatBot.domain.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long report_no; // report_no

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    private String currentDifficulty;
    private String currentEmotion;
    private String aiAdvice;

    // 비디오 링크 테이블과 관계 설정
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoLink> videoLinks = new ArrayList<>();

}
