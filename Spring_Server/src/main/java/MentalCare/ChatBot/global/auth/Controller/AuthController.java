package MentalCare.ChatBot.global.auth.Controller;

import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import MentalCare.ChatBot.global.auth.DTO.Request.AuthRequest;
import MentalCare.ChatBot.global.auth.DTO.Response.AuthResponse;
import MentalCare.ChatBot.global.auth.JWt.AuthenticateAndGenerateToken;
import MentalCare.ChatBot.global.auth.JWt.JwtTokenDto;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "회원관리 API")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticateAndGenerateToken authenticateAndGenerateToken;
    private final MemberRepository memberRepository;

    /**
     * 로그인 API
     * @param authRequest 로그인 시 필요한 정보
     * @return JWT Token (AcT+ReT)
     */
    @Operation(summary = "로그인 API", description = " username과 password 입력 시 JWT(엑세스 토큰, 리프레시 토큰) 토큰을 클라이언트 측으로 발급한다.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {

        String username = authRequest.username();
        String password = authRequest.password();
        JwtTokenDto token = authenticateAndGenerateToken.authenticateAndGenerateToken(username,password);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * 로그 아웃 API
     * @return null
     */
    @Operation(summary = "로그아웃 API", description = "로그아웃 API")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        return ResponseEntity.ok("로그아웃");
    }

    /**
     * JWT 토큰 리프레시 API
     * @param request HTTP 요청
     * @return JWT (AcT+ReT)
     */
    @Operation(summary = "엑세스 토큰 만료시 자동 (엑세스 / 리프레시)토큰 재발급 API ", description = "엑세스 토큰 만료시 자동 리프레시/엑세스 토큰 발급 API")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {

        String refreshToken =jwtUtil.extractTokenFromRequest(request);
        jwtUtil.validateToken_isTokenValid(refreshToken);

        if (refreshToken == null || !jwtUtil.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        String username = jwtUtil.extractUsername(refreshToken);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        String newAccessToken = jwtUtil.generateAccessToken(username,member.getEmail(),member.getRole());
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        JwtTokenDto token = new JwtTokenDto(newAccessToken, newRefreshToken);
        return ResponseEntity.ok(new AuthResponse(token));
    }

}
