package MentalCare.ChatBot.domain.Member.Service;

import MentalCare.ChatBot.domain.Member.DTO.Request.MemberRequest;
import MentalCare.ChatBot.domain.Member.DTO.Request.UpdateMemberDTO;
import MentalCare.ChatBot.domain.Member.DTO.Response.EveryMemberResponse;
import MentalCare.ChatBot.domain.Member.DTO.Response.MemberResponse;
import MentalCare.ChatBot.domain.Member.Role.Role;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    //회원 가입 메서드
    Long register(MemberRequest request);

    //한명의 회원 정보 조회 메서드 -  사용자 마이페이지 용
    MemberResponse getMemberById(Long id);

    //모든 회원 정보 조회 메서드 - 관리자 회원정보 관리 페이지 용
    EveryMemberResponse getEveryMemberByrole(Role role);

    //회원 정보 수정 메서드
    Long updateMember(String username , UpdateMemberDTO updateMemberDTO);

    //회원 삭제 메서드
    void deleteMember(Long member_no);

}
