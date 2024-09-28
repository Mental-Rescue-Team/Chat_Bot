package MentalCare.ChatBot.global.auth.JWt;

import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateAndGenerateToken {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /*로그인시 사용될 인증 + 토큰 생성 메서드 */
    // FIXME : 예외 처리 코드는 차후 customizing 할것
    public JwtTokenDto authenticateAndGenerateToken(String username, String password) {
        return memberRepository.findByUsername(username)
                .filter(member -> passwordEncoder.matches(password, member.getPassword()))
                .map(member -> {
                    String accessToken = jwtUtil.generateAccessToken(username);
                    String refreshToken = jwtUtil.generateRefreshToken(username);
                    return new JwtTokenDto(accessToken, refreshToken);
                })
                .orElseThrow(() -> new RuntimeException("Invalid credentials")); // 인증 실패 시 예외 처리
    }
}
