package MentalCare.ChatBot.domain.Member.Entity;

//import MentalCare.ChatBot.domain.Entity.Diary;
//import MentalCare.ChatBot.domain.Entity.Report;
import MentalCare.ChatBot.domain.Member.Role.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 필드를 포함한 생성자 생성
public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_no;

    //회원가입 시 기입-1
    private String username;

    //회원가입 시 기입-2
    private String email;

    //회원가입 시 기입-3
    private String password;

    //회원가입 시 기입-4
    private LocalDate birth;

    @Enumerated(EnumType.STRING)  // ROLE을 문자열로 저장
    private Role role;

    @Column(updatable = false)
    private LocalDateTime registerDate;

    @PrePersist  // 엔티티가 저장되기 전에 실행
    protected void onCreate() {
        this.registerDate = LocalDateTime.now();  // 등록일을 현재 시간으로 설정
        this.role = Role.USER;  // 기본 ROLE을 USER로 설정
    }

    //비밀번호 인코딩 함수
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}