package MentalCare.ChatBot.domain.Member.Controller;

import MentalCare.ChatBot.domain.Member.DTO.Request.MemberRequest;
import MentalCare.ChatBot.domain.Member.DTO.Request.UpdateMemberDTO;
import MentalCare.ChatBot.domain.Member.DTO.Response.EveryMemberResponse;
import MentalCare.ChatBot.domain.Member.DTO.Response.MemberResponse;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.domain.Member.Service.MemberService;
import MentalCare.ChatBot.global.auth.DTO.Response.ResponseVO;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Member", description = "회원관리 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtutil;
    private final MemberRepository memberRepository;

    /* 보안 이슈 : 현재 JWT에서 username을 추출하기에 만약 관리자/사용자 사이에 동명이인이 있으면, 권한 문제가 생길 수 있음 */
    /* 해결책 1 : JWT에 권한을 넣어서 권한을 추출하여 메서드를 다시 구현 */
    /* 해결책 2 : 각 메서드 별로 권한 설정을 하여 동명이인일지라도 결국은 ROLE을 확인하도록 구현 */

    @Operation(summary = " 회원가입 ", description = " 회원가입 ")
    @PostMapping("member/register")
    public ResponseEntity<String> register(@RequestBody @Valid MemberRequest request) {

        Long createdId = memberService.register(request);
        return ResponseEntity.ok().body("회원 가입 성공! member_no : " +createdId);
    }

    @Operation(summary = "(사용자 용)한 회원 정보 조회", description = "(사용자 용)한 회원 정보 조회")
    @GetMapping("/member")
    public ResponseEntity<ResponseVO<MemberResponse>> getInfo(HttpServletRequest request){

        String userToken =jwtutil.extractTokenFromRequest(request);
        jwtutil.validateToken_isTokenValid(userToken);
        MemberResponse response = memberService.getmyinfo(jwtutil.extractUsername(userToken));

        return ResponseEntity.ok(new ResponseVO<>(response,"(사용자 용)한 회원 정보 조회"));
    }

    @Operation(summary = "(관리자 용)모든 회원 정보 조회", description = "(관리자 용)모든 회원 정보 조회")
    @GetMapping("/admin")
    public ResponseEntity<ResponseVO<List<EveryMemberResponse>>> getEveryInfo(HttpServletRequest request){

        String adminToken =jwtutil.extractTokenFromRequest(request);
        jwtutil.validateToken_isTokenValid(adminToken);
        String username = jwtutil.extractUsername(adminToken);
        List<EveryMemberResponse> responseList = memberService.geteveryinfo(username);

        return ResponseEntity.ok(new ResponseVO<>(responseList,"(관리자 용)모든 회원 정보 조회"));
    }

    // TODO : 희대의 난재 -회원 정보 수정 후 바로 사용자 정보 조회 API 등 다른 api를 불러와야 하는데,
    // 정보를 수정하니 JWT 토큰에 변화가 생겨서 불가능하더라
    /* 해결방안 강구 */
    /* 대첵 1 : 회원 정보 수정 후에도 JWT 토큰이 안바뀔 수 있는지? */
    /* 대책 2 : 아니면 회원 정보 수정 후 바뀐 JWT 토큰이 자동으로 클라이언트 측에 주입이 되는 방식으로 진행할지? */
    @Operation(summary = "(사용자 용)회원 정보 수정", description = "(사용자 용)회원 정보 수정")
    @PutMapping("/member/edit")
    public ResponseEntity<String> updateMember(HttpServletRequest request,@RequestBody UpdateMemberDTO updateMemberDTO){

        String userToken =jwtutil.extractTokenFromRequest(request);
        jwtutil.validateToken_isTokenValid(userToken);
        memberService.updateMember(jwtutil.extractUsername(userToken), updateMemberDTO);

        return ResponseEntity.ok().body("회원 정보 수정 완료");
    }

    @Operation(summary = "(관리자 용)회원 정보 삭제", description = "(관리자 용) 회원 정보 삭제")
    @DeleteMapping("/members/{member_no}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable("member_no") Long member_no) {
        memberService.deleteMember(member_no);
        return ResponseEntity.ok("회원 삭제 완료");
    }
}

