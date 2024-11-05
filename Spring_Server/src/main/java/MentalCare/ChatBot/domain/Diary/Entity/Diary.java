package MentalCare.ChatBot.domain.Diary.Entity;

import MentalCare.ChatBot.domain.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryNo;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    //private Long member_no;
    @Column(length = 1000)  // 길이를 500으로 설정
    private String diaryText;
    @Column(length = 500)  // 길이를 500으로 설정
    private String diarySummary;
    @Column(length = 500)  // 길이를 500으로 설정
    private String comicURL;
    private String diaryEmotion;
    private String weather;
    private String weatherEmoji; //이모티콘은 본질적으로 유니코드 문자로 표현되며, String 타입으로 진행
    private LocalDate diaryDate;

    /*member_no를 통해 member 엔티티와 일대다 연결*/
    /*private Long member_no 필드를 위 엔티티에 넣을 필요는 없음*/

    // 생성자 추가
    /*DiaryController에서 생성자를 사용하려고 여기에 생성자 추가 메서드 작성*/
    public Diary( Member member ,String diaryText, String diarySummary, String comicURL, String diaryEmotion, String weather, String weatherEmoji, LocalDate diaryDate) {
        this.member = member ;
        this.diaryText = diaryText;
        this.diarySummary = diarySummary;
        this.comicURL = comicURL;
        this.diaryEmotion = diaryEmotion;
        this.weather = weather;
        this.weatherEmoji = weatherEmoji;
        this.diaryDate = diaryDate;
    }
}
