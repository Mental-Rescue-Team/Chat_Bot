package MentalCare.ChatBot.domain.Member.Service;

import MentalCare.ChatBot.domain.Member.DTO.Request.MemberRequest;
import MentalCare.ChatBot.domain.Member.DTO.Request.UpdateMemberDTO;
import MentalCare.ChatBot.domain.Member.DTO.Response.MemberResponse;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    /**
     * 회원 가입 메서드
     * @param request
     * @return member_no 가입된 회원의 고유번호
     */
    Long register(MemberRequest request);

    /**
     * 내 정보 조회 메서드
     * @param username
     * @return memberResponse 회원 가입 정보 객체
     */
    MemberResponse getmyinfo(String username);

    /**
     * 회원 정보 수정
     * @param username
     * @param updateMemberDTO
     */
    void updateMember(String username , UpdateMemberDTO updateMemberDTO);

}
