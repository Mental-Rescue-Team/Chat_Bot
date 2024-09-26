package MentalCare.ChatBot.global.auth.DTO.Request;

/*
 * 인증 요청에 사용되는 DTO
 */

public record AuthRequest(
        String username,
        String password
){}
