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

    //회원 가입 컨트롤러-jwt 불필요
    //메서드 구현 o
    //테스팅 o : postman
    @Operation(summary = " 회원가입 ", description = " 회원가입 ")
    @PostMapping("member/register")
    public ResponseEntity<ResponseVO<String>> register(@RequestBody @Valid MemberRequest request) {
        Long createdId = memberService.register(request);
        ResponseVO<String> response = new ResponseVO<>("Registration successful. User ID: " + createdId,"회원가입 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //(사용자 용)한 회원 정보 조회 컨트롤러 - jwt 필요
    //메서드 구현 o
    //테스팅 o : postman
    // FIXME : memberservice에 로직 분리하기
    @Operation(summary = "(사용자 용)한 회원 정보 조회", description = "(사용자 용)한 회원 정보 조회")
    @GetMapping("/member")
    public ResponseEntity<ResponseVO<MyInfoResponse>> getmyinfo(HttpServletRequest request){

        //요청에서 jwt 토큰을 가져옴
        String usertoken =jwtutil.extractTokenFromRequest(request);

        if (usertoken == null || usertoken.isEmpty())
        {throw new IllegalArgumentException("JWT token cannot be null or empty");}

        //jwt토큰에서 사용자 이름 추출
        String username = jwtutil.extractUsername(usertoken);

        //사용자 이름으로 멤버 객체 찾기
        Optional<Member> optionalMember = memberRepository.findByUsername(username);

        //Optional에서 Member 객체를 가져옴
        Member member = optionalMember.orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        //MyInfoResponse로 변환하여 반환
        MyInfoResponse response = MyInfoResponse.from(member);

        //200ok + 사용자 정보
        return ResponseEntity.ok(new ResponseVO<>(response,"(사용자 용)한 회원 정보 조회"));
    }

    //(관리자 용)모든 회원 정보 조회 컨트롤러- jwt 필요
    //메서드 구현 o
    //테스팅 o : postman
    // FIXME : memberservice에 로직 분리하기
    @Operation(summary = "(관리자 용)모든 회원 정보 조회", description = "(관리자 용)모든 회원 정보 조회")
    @GetMapping("/admin")
    public ResponseEntity<ResponseVO<List<EveryMemberResponse>>> geteveryinfo(HttpServletRequest request){
        //관리자에게서 요청에서 jwt 토큰을 가져옴
        String admintoken =jwtutil.extractTokenFromRequest(request);

        if (admintoken == null || admintoken.isEmpty())
        {throw new IllegalArgumentException("JWT token cannot be null or empty");}

        //jwt토큰에서 사용자 이름 추출
        String username = jwtutil.extractUsername(admintoken);

        //사용자 이름으로 멤버 객체 찾기
        Optional<Member> optionalMember = memberRepository.findByUsername(username);

        //Optional에서 Member 객체를 가져옴
        Member member = optionalMember.orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        // 모든 회원 정보를 조회
        List<Member> allMembers = memberRepository.findAll();

        // 모든 회원 정보를 EveryMemberResponse로 변환
        List<EveryMemberResponse> responseList = allMembers.stream()
                .map(EveryMemberResponse::from)
                .toList();

        //200ok + 사용자 정보
        return ResponseEntity.ok(new ResponseVO<>(responseList,"(관리자 용)모든 회원 정보 조회"));
    }

    //회원 정보 수정 컨트롤러- jwt 필요
    //메서드 구현 o
    //테스팅 o : postman
    @Operation(summary = "(사용자 용)회원 정보 수정", description = "(사용자 용)회원 정보 수정")
    @PutMapping("/member/edit")
    public ResponseEntity<ResponseVO<String>> updateMember(HttpServletRequest request,@RequestBody UpdateMemberDTO updateMemberDTO){

        //요청에서 jwt 토큰을 가져옴
        String usertoken =jwtutil.extractTokenFromRequest(request);

        if (usertoken == null || usertoken.isEmpty())
        {throw new IllegalArgumentException("JWT token cannot be null or empty");}

        //jwt토큰에서 사용자 이름 추출
        String username = jwtutil.extractUsername(usertoken);

        // 서비스 계층에서 회원 정보 수정 처리
        Long editedmember_no =memberService.updateMember(username, updateMemberDTO);

        ResponseVO<String> response = new ResponseVO<>( "수정된 회원 번호 = " + editedmember_no , "회원 정보 수정");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    //(관리자 용)회원 정보 삭제 메서드- jwt 필요
    //메서드 구현 o
    //테스팅 o : postman
    @Operation(summary = "(관리자 용)회원 정보 삭제", description = "(관리자 용) 회원 정보 삭제")
    @DeleteMapping("/members/{member_no}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable("member_no") Long member_no) {
        memberService.deleteMember(member_no);
        return ResponseEntity.ok("회원 삭제 완료");
    }
}

