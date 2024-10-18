package MentalCare.ChatBot.domain.Member.DTO.Response;

import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Role.Role;
import java.time.LocalDateTime;

/*
 * 관리자 용 회원 정보 조회 페이지 모든 회원 정보 조회 용 DTO
 */

public record EveryMemberResponse(
        Long member_no,
        String username,
        String email,
        LocalDateTime registerDate,
        Role role
) {
    // DTO에서 엔티티로 변환하는 메서드
    public Member toEntity() {
        return Member.builder()
                .member_no(this.member_no)
                .username(this.username)
                .email(this.email)
                .role(this.role) // Role을 String으로 변환
                .registerDate(this.registerDate)
                .build();
    }

    public static EveryMemberResponse from(Member member){
        return new EveryMemberResponse(
                member.getMember_no(),
                member.getUsername(),
                member.getEmail(),
                member.getRegisterDate(),
                member.getRole()
        );
    }
}
