package MentalCare.ChatBot.domain.Member.DTO.Response;

import MentalCare.ChatBot.domain.Member.Entity.Member;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MemberResponse(
        String username,  // 사용자 이름
        String email,     // 이메일
        LocalDate birth,      // 생년월일
        String gender
) {
    // DTO에서 엔티티로 변환하는 메서드
    public Member toEntity() {
        return Member.builder()
                .username(this.username)
                .email(this.email)
                .birth(this.birth)  // birth는 String 타입으로 가정
                .gender(this.gender)
                .build();
    }
    public static MemberResponse from(Member member){
        return new MemberResponse(
                member.getUsername(),
                member.getEmail(),
                member.getBirth(),
                member.getGender()
        );
    }
}
