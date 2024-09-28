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

    /*구현 완료*/
    private final JwtUtil jwtUtil;
    private final AuthenticateAndGenerateToken authenticateAndGenerateToken;
    private final MemberRepository memberRepository;

    @Operation(summary = "로그인 ", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {

        JwtTokenDto token = authenticateAndGenerateToken.authenticateAndGenerateToken(authRequest.username(), authRequest.password());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    //로그아웃-jwt 불필요,클라이언트 측에서 jwt 삭제
    //메서드 구현 x
    //테스팅 x
    @Operation(summary = "로그아웃 아직 미구현 ", description = "로그아웃 아직 미구현")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        //클라이언트 주도 jwt 삭제이므로 서버 측에서는 특별한 처리 x
        return ResponseEntity.ok("Logged out successfully");
    }

    @Operation(summary = "엑세스 토큰 만료시 자동 리프레시 토큰 발급 API ", description = "엑세스 토큰 만료시 자동 리프레시 토큰 발급 API")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {

        String refreshToken =jwtUtil.extractTokenFromRequest(request);

        if (refreshToken == null || refreshToken.isEmpty())
        {throw new IllegalArgumentException("JWT token cannot be null or empty");}

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
