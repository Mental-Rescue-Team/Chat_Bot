package MentalCare.ChatBot.global.auth.DTO.Response;

import MentalCare.ChatBot.global.auth.JWt.JwtTokenDto;

/*jwt 토큰 반환하는 record*/
public record AuthResponse(JwtTokenDto token) {

}
