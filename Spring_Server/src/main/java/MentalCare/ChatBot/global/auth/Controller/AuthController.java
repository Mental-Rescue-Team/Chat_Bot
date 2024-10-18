package MentalCare.ChatBot.global.auth.Controller;

import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import MentalCare.ChatBot.global.auth.DTO.Response.AuthResponse;
import MentalCare.ChatBot.global.auth.JWt.JwtTokenDto;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import MentalCare.ChatBot.global.auth.JWt.AuthenticateAndGenerateToken;
//import MentalCare.ChatBot.global.auth.Userdetails.CustomUserDetailsService;
import MentalCare.ChatBot.global.auth.DTO.Request.AuthRequest;
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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticateAndGenerateToken authenticateAndGenerateToken;
    private final MemberRepository memberRepository;

    @Operation(summary = "로그인 ", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {

        JwtTokenDto token = authenticateAndGenerateToken.authenticateAndGenerateToken(authRequest.username(), authRequest.password()); //인증이 성공하면 JWT 생성
        return ResponseEntity.ok(new AuthResponse(token)); //jwt를 클라이언트에게 반환
    }

    @Operation(summary = "로그아웃 ", description = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("클라이언트 측에서 엑세스 토큰과 리프레시 토큰을 모두 삭제해 주세요"); //클라이언트 주도 jwt 삭제이므로 서버 측에서는 특별한 처리 x
    }

    @Operation(summary = "엑세스 토큰 만료시 자동 리프레시 토큰 발급 API ", description = "엑세스 토큰 만료시 자동 리프레시 토큰 발급 API")
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

        String newAccessToken = jwtUtil.generateAccessToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        JwtTokenDto token = new JwtTokenDto(newAccessToken, newRefreshToken);
        return ResponseEntity.ok(new AuthResponse(token));
    }

}
