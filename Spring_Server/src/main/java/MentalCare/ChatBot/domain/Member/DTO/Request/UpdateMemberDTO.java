package MentalCare.ChatBot.domain.Member.DTO.Request;

import MentalCare.ChatBot.domain.Member.Entity.Member;

import java.time.LocalDate;

public record UpdateMemberDTO(
        String username,  // 사용자 이름
        String email,     // 이메일
        LocalDate birth,
        String gender
) {
    // DTO에서 엔티티로 변환하는 메서드
    public Member toEntity() {
        return Member.builder()
                .username(this.username)
                .email(this.email)
                .birth(this.birth)  // birth는 String 타입으로 가정
                .gender(this.gender != null ? this.gender : "unknown") // gender가 null이면 "unknown"으로 설정
                .build();
    }
}
