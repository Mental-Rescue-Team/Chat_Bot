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

    /*회원 가입 메서드*/
    Long register(MemberRequest request);

    /*한명의 회원 정보 조회 메서드 -  사용자 마이페이지 용*/
    MemberResponse getmyinfo(String username);/*사용자 이름으로 본인의 회원 정보를 조회할 시 동명이인의 경우 예외사항이 발생한다, 즉 이 메서드는 파라매터를 member_no를 받는 로직으로 추후 수정하여야 한다.*/

    /*회원 정보 수정 메서드*/
    void updateMember(String username , UpdateMemberDTO updateMemberDTO);


}
