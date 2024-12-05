package MentalCare.ChatBot.domain.Admin.Controller;

import MentalCare.ChatBot.domain.Admin.Service.AdminService;
import MentalCare.ChatBot.domain.Chatbot.DTO.Response.ReportDTO;
import MentalCare.ChatBot.domain.Chatbot.Repository.ReportRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "Admin", description = " 관리자 페이지 API")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final JwtUtil jwtUtil;
    private final AdminService adminService;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final DiaryService diaryService;

    /**
     * 모든 사용자의 정보 조회 API
     * @param request 사용자 요청
     * @return 모든 사용자의 정보
     */
    @Operation(summary = "(관리자 용)모든 회원 정보 조회", description = "(관리자 용)모든 회원 정보 조회")
    @GetMapping("")
    public ResponseEntity<ResponseVO<List<EveryMemberResponse>>> getEveryInfo(HttpServletRequest request){

        String adminToken =jwtUtil.extractTokenFromRequest(request);
        jwtUtil.validateToken_isTokenValid(adminToken);

        String username = jwtUtil.extractUsername(adminToken);
        List<EveryMemberResponse> responseList = adminService.getEveryInfo(username);

        return ResponseEntity.ok(new ResponseVO<>(responseList,"(관리자 용)모든 회원 정보 조회"));
    }

    /**
     * 회원 정보 삭제 API
     * @param member_no 사용자 고유 번호
     * @return 회원 정보 삭제
     */
    @Operation(summary = "(관리자 용)회원 정보 삭제", description = " 관리자 페이지에서 삭제 버튼 클릭 시 호출되는 API이다. 삭제 버튼 시 클라이언트 측에서는 서버 측으로 사용자의 고유 번호인 member_no 값을 넘겨줘야 삭제가 가능하다.")
    @DeleteMapping("/{member_no}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable("member_no") Long member_no) {

        adminService.deleteMember(member_no);

        return ResponseEntity.ok("회원 삭제 완료");
    }

    /**
     * 모든 회원의 이름 조회 API
     * @return 모든 회원 이름 조회
     */
    @Operation(summary = " 모든 유저 이름 조회", description = " 이 API는 이 페이지가 화면에 로딩됨과 동시에 호출되어야 하는 API 이며, 클라이언트 측에서 서버 측으로 전송할 데이터는 없습니다. 이 이름 하나를 클릭 시 해당 인원의 모든 레포트 기록이 표츌이 된다.")
    @GetMapping("/names")
    public List<String> getEveryNames(){

        List<String> names = memberRepository.findAllUsernames();

        return memberRepository.findAllUsernames();
    }

    /**
     * 해당 사용자의 모든 AI 레포트 조회 API
     * @param username 사용자의 이름
     * @return 헤당 사용자의 모든 AI레포트
     */
    @Operation(summary = " 해당 사용자의 모든 AI 레포트 조회", description = " 유저의 이름을 클릭시, 클라이언트 측에서 해당 유저의 이름을 서버측에 전송해줘야 합니다, 유저의 모든 Report를 로드해온다.")
    @GetMapping("/reports/{username}")
    public List<ReportDTO> getEveryReports(@PathVariable("username") String username){

        Member member= memberRepository.findByUsername(username)
                .orElseThrow(()->new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        return reportRepository.findReportDTOByMemberNo(member.getMember_no());
    }

    /**
     * 일기 분석 통계자료 조회 API
     * @return 일기 통계 반환
     */
    @Operation(summary = " 일기 분석 통계자료 조회", description = " 클라이언트 측에서는 아무것도 전송할것이 없음 -서버 측에서 일기 분석 통계자료를 모두 불러온다 ")
    @GetMapping("/emotions")
    public Map<String, Long> getEveryEmotion(){

        Map<String, Long> emotionCounts = diaryService.getEmotionCounts();
        emotionCounts.forEach((emotion, count) ->
                log.info("Emotion : {} , Count : {} " , emotion,count)
        );

        return emotionCounts;
    }

}
