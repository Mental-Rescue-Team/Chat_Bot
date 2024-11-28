package MentalCare.ChatBot.global.auth.JWt;

import MentalCare.ChatBot.domain.Member.Role.Role;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.accessexpiration}")
    private long ACCESS_EXPIRATION_TIME;

    @Value("${jwt.refreshexpiration}")
    private long REFRESH_EXPIRATION_TIME;

    /**
     * 엑세스 토큰 생성 메서드
     * @param username 사용자 이름
     * @return AccessToken 엑세스 토큰
     */
    public String generateAccessToken(String username,String email,Role role) {

        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + ACCESS_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(expiryDate)
                .claim("email", email)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    /**
     * 리프레시 토큰 생성 메서드
     * @param username 사용자 이름
     * @return RefreshToken 리프레시 토큰
     */
    public String generateRefreshToken(String username) {

        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + REFRESH_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    /**
     * Claim(정보) 추출 메서드
     * @param token jwt 토큰
     * @return claim jwt 내의 사용자 정보
     * @see #extractUsername(String) 토큰에서 username 추출 메서드
     * @see #isTokenExpired(String) 토큰 만료여부 확인 메서드
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()// JWT 파서 객체 생성
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * jwt 토큰에서 유저 네임 추출
     * @param token JWT 토큰
     * @return
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * 사용자 요청에서 jwt 토큰 추출 메서드
     * @param request 사용자 요청
     * @return
     */
    public String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        log.info("header from method extractTokenFromRequest : {} ",header);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    /**
     * 토큰 만료 여부 확인
     * @param token JWT 토큰
     * @return
     */
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * 리프레시 토큰 유효성 검사
     * @param refreshToken 리프레시 토큰
     * @return
     */
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = extractClaims(refreshToken);
            return !isTokenExpired(refreshToken);
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 사용자 HTTP 요청에서 이름 추출 메서드
     */
    public String extractNameByRequest(){

        return null;
    }

    /**
     * 토큰의 유효성을 검증 메서드 - 사용자 이름과 일치하는지 확인
     * @param token JWT 토큰
     * @param username 사용자 이름
     * @return
     */
    public boolean validateToken_isUsernameMatching(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * 토큰의 유효성 검증 메서드 - 토큰이 비어있는지 null값인지 확인
     * @param token JWT 토큰
     */
    public void validateToken_isTokenValid(String token){
        log.info(" ##### ##### Checking your token is valid ##### #####");
        log.info("Your validated token is : {}" ,token);
        if (token == null || token.isEmpty()) {
            throw new MemberException(ErrorCode.TOKEN_MUST_FILLED);
        }
    }

    //TODO :jwtutil 에서 extractNameByRequest 메서드를 만들어서
    // memberController의 getinfo 메서드의 컨트롤러 코드를 간소화 하려 했음
    // by 2024.11.13
}
