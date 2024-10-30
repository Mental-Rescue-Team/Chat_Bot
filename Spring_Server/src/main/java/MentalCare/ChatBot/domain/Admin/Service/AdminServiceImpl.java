package MentalCare.ChatBot.domain.Admin.Service;

import MentalCare.ChatBot.domain.Member.DTO.Response.EveryMemberResponse;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final MemberRepository memberRepository;

    //모든 회원 정보 조회 메서드 - 관리자 회원정보 관리 페이지 용
    @Override
    public List<EveryMemberResponse> getEveryInfo(String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()->new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        List<Member> allMembers = memberRepository.findAll();

        // 모든 회원 정보를 EveryMemberResponse 로 변환
        return allMembers.stream()
                .map(EveryMemberResponse::from)
                .toList();
    }

    /* 회원 정보 삭제 메서드 구현 */
    @Transactional
    @Override
    public void deleteMember(Long member_no) {
        Member member = memberRepository.findById(member_no)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
        memberRepository.delete(member);
    }
}
