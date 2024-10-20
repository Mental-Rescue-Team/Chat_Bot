package MentalCare.ChatBot.global.auth.JWt;

import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.accessexpiration}")
    private long ACCESS_EXPIRATION_TIME;

    @Value("${jwt.refreshexpiration}")
    private long REFRESH_EXPIRATION_TIME;

    /* 엑세스 토큰 생성 메서드 */
    public String generateAccessToken(String username) {
        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + ACCESS_EXPIRATION_TIME); // 짧은 유효 기간

        //리소스 접근 허용을 위한 사용자 데이터들을 담는다
        return Jwts.builder()
                .setSubject(username)
//                .claim("userId", ) // 사용자 ID 추가
//                .claim("email", email) // 이메일 추가
                .setIssuedAt(new Date(now))
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    /* 리프레시 토큰 생성 메서드 */
    public String generateRefreshToken(String username) {
        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + REFRESH_EXPIRATION_TIME); // 긴 유효 기간

        /*jwt 토큰 재발급을 위한 데이터를 담는다*/
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    /* 밑에 extractUsername와 isTokenExpired에 사용는 클레임(즉, 정보를) 추출하는 역할  */
    public Claims extractClaims(String token) {
        return Jwts.parser()// JWT 파서 객체 생성
                .setSigningKey(SECRET_KEY) // 서명 검증을 위한 비밀 키 설정
                .parseClaimsJws(token)// JWT 문자열을 파싱하여 ClaimsJws 객체로 변환
                .getBody();  // ClaimsJws 객체에서 클레임 본문 추출
    }

    /* 토큰에서 유저 네임 추출 */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /* 요청에서 토큰 추출 메서드 */
    public String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // "Bearer " 이후의 문자열을 반환
        }
        return null; // 토큰이 없을 경우
    }

    /* 토큰 만료 여부 확인 */
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /* 리프레시 토큰 유효성 검사 */
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = extractClaims(refreshToken);// 리프레시 토큰의 클레임을 추출하여 유효성을 검증
            return !isTokenExpired(refreshToken);// 만료 여부 확인
        }
        catch (Exception e) {
            return false; // 토큰이 유효하지 않은 경우 예외 처리
        }
    }

    /* Validation Method Below */

    /* 토큰의 유효성을 검증 - 사용자 이름과 일치하는지 확인 */
    public boolean validateToken_isUsernameMatching(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /* 토큰의 유효성 검증 - 토큰이 비어있는지 null값인지 확인 */
    public void validateToken_isTokenValid(String token){
        if (token == null || token.isEmpty()) {
            throw new MemberException(ErrorCode.TOKEN_MUST_FILLED);
        }
    }
}
