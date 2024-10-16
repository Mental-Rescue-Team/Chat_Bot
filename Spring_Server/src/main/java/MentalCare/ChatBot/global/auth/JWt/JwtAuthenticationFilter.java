package MentalCare.ChatBot.global.auth.JWt;

import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    /*http 요청이 들어올 때 마다, 요청 헤더에 포함 된 jwt 토큰을 확인하고 처리 */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        //requset에서 Authorization헤더를 추출-> 있으면 그 값을 반환, 없으면 NULL 값 반환
        //JWT 인증에서는 Authorization 헤더에 "Bearer {token}" 형식으로 JWT 토큰을 포함
        //Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

        String username = null;
        String jwt = null;

        /*인증 헤더 존재 && "Bearer"로 시작*/
        //jwt와 사용자 이름을 저장한다.
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        /*사용자 이름은 있으나, 인증이 아직 되지 않았을때*/
        //사용자 정보를 로드하여 JWT의 유효성을 검증합니다.
        //토큰이 유효하면 사용자 세부 정보와 권한을 포함한 인증 객체를 생성하여 **SecurityContextHolder**에 저장합니다.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

            if (jwtUtil.validateToken(jwt, member.getUsername())) {
                // 인증 설정
                Collection<? extends GrantedAuthority> authorities =
                        Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name()));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(member, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 다음 필터로 요청 전달
        chain.doFilter(request, response);
    }
}
