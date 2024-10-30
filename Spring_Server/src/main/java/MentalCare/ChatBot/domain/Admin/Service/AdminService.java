package MentalCare.ChatBot.domain.Admin.Service;

import MentalCare.ChatBot.domain.Member.DTO.Response.EveryMemberResponse;

import java.util.List;

public interface AdminService {
    /*모든 회원 정보 조회 메서드 - 관리자 회원정보 관리 페이지 용*/
    List<EveryMemberResponse> getEveryInfo(String username); /*관리자 이름으로 모든 회원의 회원 정보를 조회할 시 동명이인의 사용자가 있을 경우 예외사항이 발생하기에, 즉 이 메서드는 파라매터를 member_no를 받는 로직으로 추후 수정하하.*/

    //회원 삭제 메서드*/
    void deleteMember(Long member_no);


}
