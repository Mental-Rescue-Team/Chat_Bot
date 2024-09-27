package MentalCare.ChatBot.domain.Member.Controller;

import MentalCare.ChatBot.domain.Member.DTO.Request.MemberRequest;
import MentalCare.ChatBot.domain.Member.DTO.Request.UpdateMemberDTO;
import MentalCare.ChatBot.domain.Member.DTO.Response.EveryMemberResponse;
import MentalCare.ChatBot.domain.Member.DTO.Response.MyInfoResponse;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.domain.Member.Service.MemberService;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import MentalCare.ChatBot.global.auth.DTO.Response.ResponseVO;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import io.jsonwebtoken.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Tag(name = "Member", description = "회원관리 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtutil;
    private final MemberRepository memberRepository;

    // FIXME : 현재 USER의 한 사용자 정보 조회와, ADMIN의 모든 사용자 정보 조회 시 jwt에서 role이 아니라 이름으로 추출한다.
    /*보안상 문제가 된다. username은 기본적으로 중복이 허용되는데, 관리자와 우연히 이름이 같은 사용자는 관리자 페이지로 접근이 가능하기 때문이다.*/

    @Operation(summary = " 회원가입 ", description = " 회원가입 ")
    @PostMapping("member/register")
    public ResponseEntity<ResponseVO<String>> register(@RequestBody @Valid MemberRequest request) {
        Long createdId = memberService.register(request);
        ResponseVO<String> response = new ResponseVO<>("Registration successful. User ID: " + createdId,"회원가입 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // FIXME : memberservice에 로직 분리하기
    @Operation(summary = "(사용자 용)한 회원 정보 조회", description = "(사용자 용)한 회원 정보 조회")
    @GetMapping("/member")
    public ResponseEntity<ResponseVO<MyInfoResponse>> getmyinfo(HttpServletRequest request){

        String usertoken =jwtutil.extractTokenFromRequest(request);
        if (usertoken == null || usertoken.isEmpty()) {
            throw new IllegalArgumentException("JWT token cannot be null or empty");
        }

        String username = jwtutil.extractUsername(usertoken);
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        Member member = optionalMember.orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        MyInfoResponse response = MyInfoResponse.from(member);

        //200ok + 사용자 정보
        return ResponseEntity.ok(new ResponseVO<>(response,"(사용자 용)한 회원 정보 조회"));
    }

    // FIXME : memberservice에 로직 분리하기
    @Operation(summary = "(관리자 용)모든 회원 정보 조회", description = "(관리자 용)모든 회원 정보 조회")
    @GetMapping("/admin")
    public ResponseEntity<ResponseVO<List<EveryMemberResponse>>> geteveryinfo(HttpServletRequest request){

        String admintoken =jwtutil.extractTokenFromRequest(request);
        if (admintoken == null || admintoken.isEmpty()) {
            throw new MemberException(ErrorCode.JWT_CANNOT_BE_NULL);
        }

        String username = jwtutil.extractUsername(admintoken);
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        Member member = optionalMember.orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        List<Member> allMembers = memberRepository.findAll();
        List<EveryMemberResponse> responseList = allMembers.stream()
                .map(EveryMemberResponse::from)
                .toList();

        //200ok + 사용자 정보
        return ResponseEntity.ok(new ResponseVO<>(responseList,"(관리자 용)모든 회원 정보 조회"));
    }

    @Operation(summary = "(사용자 용)회원 정보 수정", description = "(사용자 용)회원 정보 수정")
    @PutMapping("/member/edit")
    public ResponseEntity<ResponseVO<String>> updateMember(HttpServletRequest request,@RequestBody UpdateMemberDTO updateMemberDTO){

        String usertoken =jwtutil.extractTokenFromRequest(request);
        if (usertoken == null || usertoken.isEmpty()) {
            throw new MemberException(ErrorCode.JWT_CANNOT_BE_NULL);
        }

        String username = jwtutil.extractUsername(usertoken);
        Long editedmember_no =memberService.updateMember(username, updateMemberDTO);

        ResponseVO<String> response = new ResponseVO<>( "수정된 회원 번호 = " + editedmember_no , "회원 정보 수정");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "(관리자 용)회원 정보 삭제", description = "(관리자 용) 회원 정보 삭제")
    @DeleteMapping("/members/{member_no}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable("member_no") Long member_no) {
        memberService.deleteMember(member_no);
        return ResponseEntity.ok("회원 삭제 완료");
    }
}

