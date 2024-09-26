package MentalCare.ChatBot.global.auth.Controller;

import MentalCare.ChatBot.global.auth.DTO.Response.AuthResponse;
import MentalCare.ChatBot.global.auth.JWt.JwtTokenDto;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import MentalCare.ChatBot.global.auth.JWt.AuthenticateAndGenerateToken;
//import MentalCare.ChatBot.global.auth.Userdetails.CustomUserDetailsService;
import MentalCare.ChatBot.global.auth.DTO.Request.AuthRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "회원관리 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class Login_Logout_Controller {

    private final JwtUtil jwtUtil;
    //private final CustomUserDetailsService userDetailsService;
    private final AuthenticateAndGenerateToken authenticateAndGenerateToken;

    //로그인-jwt불필요,jwt 발급 - access token,refreshtoken 모두 발급
    //메서드 구현 o
    //테스팅 o : postman
    @Operation(summary = "로그인 ", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {

        //인증이 성공하면 JWT 생성
        JwtTokenDto token = authenticateAndGenerateToken.authenticateAndGenerateToken(authRequest.username(), authRequest.password());

        //jwt를 클라이언트에게 반환
        return ResponseEntity.ok(new AuthResponse(token));
    }

    //로그아웃-jwt 불필요,클라이언트 측에서 jwt 삭제
    //메서드 구현 x
    //테스팅 x
    @Operation(summary = "로그아웃 ", description = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        //클라이언트 주도 jwt 삭제이므로 서버 측에서는 특별한 처리 x
        return ResponseEntity.ok("Logged out successfully");
    }

}
