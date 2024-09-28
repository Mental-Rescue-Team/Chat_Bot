package MentalCare.ChatBot.global.auth.JWt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "jwt.secret=Passw0rda2024aS3cur3K3yaG3n3rat0z1x2c3v4b5n6m7a1s2d3f4g5h6j7qpwoeirutyalskdjfhgg1029384756qpeirosnvl1n4l5k6n2l35kj")
class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil(); // 필요한 의존성 주입

    @Test
    void generateAccessToken() {
    }

    @Test
    void generateRefreshToken() {
    }

    @Test
    void extractClaims() {
    }

    @Test
    void extractUsername() {
    }

    @Test
    void extractTokenFromRequest() {
    }

    @Test
    void isTokenExpired() {
    }

    @Test
    void validateToken() {
        String username = "rsy_user";
        String token = jwtUtil.generateAccessToken(username); // 유효한 토큰 생성

        boolean isValid = jwtUtil.validateToken(token, username);
        assertTrue(isValid, "토큰이 유효해야 합니다.");
    }
}