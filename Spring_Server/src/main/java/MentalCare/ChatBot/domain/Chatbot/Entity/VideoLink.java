package MentalCare.ChatBot.domain.Chatbot.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoLink_no;

    private String video_link1;
    private String video_link2;

    @ManyToOne
    @JoinColumn(name = "report_no")
    private Report report;


}
