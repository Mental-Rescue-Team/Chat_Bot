package MentalCare.ChatBot.domain.Admin.Controller;

import MentalCare.ChatBot.domain.Admin.Service.AdminService;
import MentalCare.ChatBot.domain.Chatbot.DTO.Response.ReportDTO;
import MentalCare.ChatBot.domain.Chatbot.Repository.ReportRepository;
import MentalCare.ChatBot.domain.Diary.Repository.DiaryRepository;
import MentalCare.ChatBot.domain.Diary.Service.DiaryService;
import MentalCare.ChatBot.domain.Member.DTO.Response.EveryMemberResponse;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import MentalCare.ChatBot.global.auth.DTO.Response.ResponseVO;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Admin", description = " 관리자 페이지 API")
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    // TODO: 보안의 완성을 위해, 추후 권한 설정을 진행한다.

    private final JwtUtil jwtUtil;
    private final AdminService adminService;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryService diaryService;

    /* 모든 사용자 정보 조회 */
    @Operation(summary = "(관리자 용)모든 회원 정보 조회", description = "(관리자 용)모든 회원 정보 조회")
    @GetMapping("/admin")
    public ResponseEntity<ResponseVO<List<EveryMemberResponse>>> getEveryInfo(HttpServletRequest request){

        String adminToken =jwtUtil.extractTokenFromRequest(request);
        jwtUtil.validateToken_isTokenValid(adminToken);
        String username = jwtUtil.extractUsername(adminToken);
        List<EveryMemberResponse> responseList = adminService.getEveryInfo(username);

        return ResponseEntity.ok(new ResponseVO<>(responseList,"(관리자 용)모든 회원 정보 조회"));
    }

    /* 회원 정보 삭제 */
    @Operation(summary = "(관리자 용)회원 정보 삭제", description = " 관리자 페이지에서 삭제 버튼 클릭 시 호출되는 API이다. 삭제 버튼 시 클라이언트 측에서는 서버 측으로 사용자의 고유 번호인 member_no 값을 넘겨줘야 삭제가 가능하다.")
    @DeleteMapping("/admin/{member_no}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable("member_no") Long member_no) {
        adminService.deleteMember(member_no);
        return ResponseEntity.ok("회원 삭제 완료");
    }

    /* 모든 유저 이름 조회 */
    @Operation(summary = " 모든 유저 이름 조회", description = " 모든 등록된 유저의 이름들을 나열한다 . 이 이름 하나를 클릭 시 해당 인원의 모든 레포트 기록이 표츌이 된다.")
    @GetMapping("/admin/names")
    public List<String> getEveryNames(){

        List<String> names = memberRepository.findAllUsernames();
        System.out.println(names);
        return memberRepository.findAllUsernames();
    }

    /* 해당 사용자의 모든 AI 레포트 조회 */
    @Operation(summary = " 해당 사용자의 모든 AI 레포트 조회", description = " 유저의 이름을 클릭시, 해당 유저의 이름을 서버측에 전달하여, 유저의 모든 Report를 로드해욘다.")
    @GetMapping("/admin/reports/{username}")
    public List<ReportDTO> getEveryReports(@PathVariable("username") String username){

        Member member= memberRepository.findByUsername(username)
                .orElseThrow(()->new MemberException(ErrorCode.NOT_FOUND_MEMBER));
        Long member_no = member.getMember_no();

        return reportRepository.findReportDTOByMemberNo(member_no);
    }

    /* 일기 분석 통계자료 조회 */
    @Operation(summary = " 일기 분석 통계자료 조회", description = " ")
    @GetMapping("/admin/emotions")
    public Map<String, Long> getEveryEmotion(){

        //기쁨, 평온, 분노, 슬픔, 불안 각각에 대한 숫자를 가져온다.
        //각 감정에 대해 퍼센테이지를 부여한다.
        //우선 출력값은 각 감정에 대한 숫자와 전체 감정의 총 합계만 반환한다.
        Map<String, Long> emotionCounts = diaryService.getEmotionCounts();
        emotionCounts.forEach((emotion, count) ->
                System.out.println("Emotion: " + emotion + ", Count: " + count)
        );

        return emotionCounts;
    }


}
