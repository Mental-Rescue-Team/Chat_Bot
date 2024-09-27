package MentalCare.ChatBot.domain.Member.Service;

import MentalCare.ChatBot.domain.Member.DTO.Request.MemberRequest;
import MentalCare.ChatBot.domain.Member.DTO.Request.UpdateMemberDTO;
import MentalCare.ChatBot.domain.Member.DTO.Response.EveryMemberResponse;
import MentalCare.ChatBot.domain.Member.DTO.Response.MemberResponse;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.domain.Member.Role.Role;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtutil;

    //회원 가입
    @Override
    public Long register(MemberRequest request) {

        Member member = request.toEntity();
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new MemberException(ErrorCode.ALREADY_EXIST_MEMBER);
        }
        member.encodePassword(passwordEncoder);
        return memberRepository.save(member).getMember_no();
    }

    //회원 정보 수정 메서드 구현
    @Override
    public Long updateMember(String username , UpdateMemberDTO request) {

        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        Member member = optionalMember.orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        // FIXME : 엔티티에 set함수는 권장되지 않으니 시간 되면 수정할 것
        if (member.getUsername() != null) {
            member.setUsername(request.username());
        }
        if (member.getEmail() != null) {
            member.setEmail(request.email());
        }
        if (member.getBirth() != null) {
            member.setBirth(request.birth());
        }

        return memberRepository.save(member).getMember_no();

    }

    //회원 정보 삭제 메서드 구현
    @Transactional
    @Override
    // FIXME : 모든 로직이 membercontroller에 전부 구현되어 있으니, 여기에 분담하여 관리할 것
    public void deleteMember(Long member_no) {
        Member member = memberRepository.findById(member_no)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
        memberRepository.delete(member);
    }

    //회원 정보 조회 메서드 구현
    @Override
    // FIXME : 모든 로직이 membercontroller에 전부 구현되어 있으니, 여기에 분담하여 관리할 것
    public MemberResponse getMemberById(Long id) {
        return null;
    }

    //모든 회원 정보 조회 메서드 - 관리자 회원정보 관리 페이지 용
    @Override
    public EveryMemberResponse getEveryMemberByrole(Role role) {
        return null;
    }
}
