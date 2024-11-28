package MentalCare.ChatBot.domain.Member.Controller;

import MentalCare.ChatBot.domain.Member.DTO.Request.MemberRequest;
import MentalCare.ChatBot.domain.Member.DTO.Response.MemberResponse;
import MentalCare.ChatBot.domain.Member.Service.MemberService;
import MentalCare.ChatBot.global.auth.DTO.Response.ResponseVO;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원관리 API")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtutil;

    /**
     * 회원 가입 api
     * @param request 회원가입 객체
     * @return member_no 가입된 회원의 고유번호
     */
    @Operation(summary = " 회원가입 ", description = " 회원 가입 시 username,password, email, birth, gender 이렇게 5가지를 입력 ")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid MemberRequest request) {

        Long createdId = memberService.register(request);

        return ResponseEntity.ok().body("회원 가입 성공! member_no : " +createdId);
    }

    /**
     * 본인 정보 조회 api
     * @param request 사용자 요청
     * @return memberResponse 회원가입 정보 객체
     */
    @Operation(summary = "본인 정보 조회", description = "사용자가 마이페이지에서 본인의 회원 정보를 조회할 수 있는 기능 - username, email, birth, gender 이렇게 4개를 조회 가능")
    @GetMapping("")
    public ResponseEntity<ResponseVO<MemberResponse>> getInfo(HttpServletRequest request){

        /*
        * jwt util에서 메서드 추가해서 간소화 하기
        * 요청에서 토큰을 추출
        * 토큰 유효성 검사
        * 토큰에서 이름 추출
        * */
        String userToken =jwtutil.extractTokenFromRequest(request);
        jwtutil.validateToken_isTokenValid(userToken);
        String username = jwtutil.extractUsername(userToken);


        MemberResponse response = memberService.getmyinfo(username);

        return ResponseEntity.ok(new ResponseVO<>(response,"(사용자 용)한 회원 정보 조회"));
    }

}

