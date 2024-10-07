package MentalCare.ChatBot.domain.Member.Service;

import MentalCare.ChatBot.domain.Member.DTO.Request.MemberRequest;
import MentalCare.ChatBot.domain.Member.DTO.Request.UpdateMemberDTO;
import MentalCare.ChatBot.domain.Member.DTO.Response.EveryMemberResponse;
import MentalCare.ChatBot.domain.Member.DTO.Response.MemberResponse;
import MentalCare.ChatBot.domain.Member.Role.Role;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MemberService {

    //회원 가입 메서드
    Long register(MemberRequest request);

    // FIXME : 사용자 이름으로 본인의 회원 정보를 조회할 시 동명이인의 경우 예외사항이 발생한다, 즉 이 메서드는 파라매터를 member_no를 받는 로직으로 추후 수정하여야 한다.
    //한명의 회원 정보 조회 메서드 -  사용자 마이페이지 용
    MemberResponse getmyinfo(String username);

    // FIXME : 관리자 이름으로 모든 회원의 회원 정보를 조회할 시 동명이인의 사용자가 있을 경우 예외사항이 발생하기에, 즉 이 메서드는 파라매터를 member_no를 받는 로직으로 추후 수정하하.
    //모든 회원 정보 조회 메서드 - 관리자 회원정보 관리 페이지 용
    List<EveryMemberResponse> geteveryinfo(String username);

    //회원 정보 수정 메서드
    Long updateMember(String username , UpdateMemberDTO updateMemberDTO);

    //회원 삭제 메서드
    void deleteMember(Long member_no);

}
