package MentalCare.ChatBot.global.auth.JWt;

import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateAndGenerateToken {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 로그인 인증 + JWT 반환 메서드
     * @param username 사용자 이름
     * @param password 비밀 번호
     * @return JWT (AcT+ReT)
     */
    public JwtTokenDto authenticateAndGenerateToken(String username, String password) {

        return memberRepository.findByUsername(username)
                .filter(member -> passwordEncoder.matches(password, member.getPassword()))
                .map(member -> {
                    String accessToken = jwtUtil.generateAccessToken(username,member.getEmail(),member.getRole());
                    String refreshToken = jwtUtil.generateRefreshToken(username);
                    return new JwtTokenDto(accessToken, refreshToken);
                })
                .orElseThrow(() -> new MemberException(ErrorCode.FAILED_TO_LOGIN));
    }
}
