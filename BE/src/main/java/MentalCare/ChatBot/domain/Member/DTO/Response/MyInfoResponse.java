package MentalCare.ChatBot.domain.Member.DTO.Response;

import MentalCare.ChatBot.domain.Member.Entity.Member;

import java.time.LocalDate;

public record MyInfoResponse(
        String username,
        String email,
        LocalDate birth
) {
    public static MyInfoResponse from(Member member){
        return new MyInfoResponse(
                member.getUsername(),
                member.getEmail(),
                member.getBirth()
        );
    }


}