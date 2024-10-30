package MentalCare.ChatBot.domain.Member.Service;

import MentalCare.ChatBot.domain.Member.DTO.Request.MemberRequest;
import MentalCare.ChatBot.domain.Member.DTO.Request.UpdateMemberDTO;
import MentalCare.ChatBot.domain.Member.DTO.Response.EveryMemberResponse;
import MentalCare.ChatBot.domain.Member.DTO.Response.MemberResponse;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtutil;

    /* 회원 가입 */
    @Override
    public Long register(MemberRequest request) {

        Member member = request.toEntity();
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new MemberException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        member.encodePassword(passwordEncoder);

        return memberRepository.save(member).getMember_no();
    }

    /* 회원 정보 수정 메서드 구현 */

    @Override
    public void updateMember(String username , UpdateMemberDTO request) {


        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        Member member = optionalMember.orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        // 엔티티에 set함수는 권장되지 않으니 시간 되면 수정할 것
        if (member.getUsername() != null) {
            member.setUsername(request.username());
        }
        if (member.getEmail() != null) {
            member.setEmail(request.email());
        }
        if (member.getBirth() != null) {
            member.setBirth(request.birth());
        }
        if (member.getGender() != null) {
            member.setGender(request.gender());
        }
        memberRepository.save(member);
    }



    /* 회원 정보 조회 메서드 구현 */
    @Override
    public MemberResponse getmyinfo(String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()->new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        return MemberResponse.from(member);
    }


}
