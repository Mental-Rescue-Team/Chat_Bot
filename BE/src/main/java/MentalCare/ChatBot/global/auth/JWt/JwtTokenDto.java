package MentalCare.ChatBot.global.auth.JWt;

public record JwtTokenDto(
        String accessToken,
        String refreshToken) {
}
