package MentalCare.ChatBot.domain.Member.Controller;

import MentalCare.ChatBot.domain.Member.DTO.Request.MemberRequest;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.domain.Member.Service.MemberService;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")  // "test" 프로파일을 활성화하여 H2 설정을 적용
@Transactional //만약 롤백을 원하지 않는 메서드의 경우 @Rollback(false)선언
class MemberControllerTest {

    @InjectMocks //실제 테스팅 대상
    private MemberController memberController;

    @Mock //테스팅 대상에 주입 될 객체
    private MemberService memberService;
    /* 가짜 객체이다. 진짜, memberService를 가져오는 것이 아니라, 추후 이 테스트 코드에서 이 가짜 객체의 행동을 직접 정의해줘야 한다.*/

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;
    //실제 웹 서버를 띄우지 않고, 컨트롤러를 테스팅 할 있는 클래스이다.
    //MockMvc를 통해 가상의 HTTP 요청을 보내서 컨트롤러의 동작을 테스트할 수 있습니다.
    //실제 웹 서버 없이 테스트할 수 있기 때문에 속도가 빠르고 효율적입니다.

    private ObjectMapper objectMapper = new ObjectMapper() // ObjectMapper를 수동으로 생성
            .registerModule(new JavaTimeModule());
    //jackson 라이브러리의 클래스이다.
    //json데이터 <-> 자바 객체로 변환하는 클래스이다.
    //직렬화 : 자바 객체 -> json 형태의 데이터
    //역직렬화: json 형태의 데이터 -> 자바 객체


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    /* <<회원 가입 테스트 시나리오>> */
    //Mock 정의 : memberService의 register정의

//    1.정상적인 입력
//    2.유효하지 않은 비밀번호 입력 - 비밀번호는 6자리 숫자 이상이여야 하는 메시지
//    3.유효하지 않는 이메일 입력 -이메일은 유효해야 한다는 메시지
//    4.유효하지 않는 입력(username 입력 x) - notBlank에 대한 메시지
//    5.예외처리 (이미 등록된 이메일로 등록) - AlREADT EXIST MEMBER 에러
//    6.의존성 모킹 검증 - verify(memberService).register(memberRequest)로 메서드가 잘 호출되었는지 확인

    @Test //정상적인 입력
    void 회원가입_테스트1() {

        // Given
        MemberRequest request = MemberRequest.builder()
                .username("testUser")
                .password("test2024^^") //비밀번호 형식에 맞지 않는 입력값
                .email("test@test.com")
                .birth(LocalDate.of(1990, 1, 1))
                .gender("male")
                .build();
        Long createdId = 0L;
        /* mockito의 when-then패턴을 사용함 */
        /* membserService는 목 객체이기 때문에, 실제 memberService를 가져온 것이 아니라 지금 이 객체의 행동을 정의하는 것이다. */
        when(memberService.register(request)).thenReturn(createdId);

        // When
        /* 여기서 memberController의 register는 실제 컨트롤러의 메서드이다. */
        /* 위의 다른 메서드들은 행동을 직접 지정함 - 그것들을 테스팅하는 것이 아니기 때문에 */
        /* 단위 테스트의 원칙에 따라 나머지 영역에 대한 의존성을 없애기 위해... */

        /* 하지만, memberController에 대한 부분은 실제 메서드를 가져온다 */
        /* 이유는 지금 그것을 단위테스팅 하는 것이기에 실제로 가져와야지 테스팅을 할 수있기 떄문이다. */
        ResponseEntity<String> response = memberController.register(request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("회원 가입 성공! member_no : " +createdId);

    }
    @Test //유효하지 않은 비밀번호 입력
    void 회원가입_테스트2()throws Exception{

        MemberRequest request = MemberRequest.builder()
                .username("testUser")
                .password("1234") //비밀번호 형식에 맞지 않는 입력값
                .email("test@test.com")
                .birth(LocalDate.of(1990, 1, 1))
                .gender("male")
                .build();

        // When & Then: 비밀번호 유효성 검사 실패
        mockMvc.perform(MockMvcRequestBuilders.post("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON) //컨텐츠 타입 헤더를 Json 형식으로 설정하는 부분이다. -> 여기서는 json 형식으로 데이터를 서버에 전송한다는 의미이다.
                        .content(objectMapper.writeValueAsString(request))) //memberrequest라는 DTO 자바 객체를 Json 문자열로 변환하는 작업을 한 후 서버로 보내는 것이다.
                .andExpect(status().isBadRequest()) // 유효성 검사 실패로 인해 400 Bad Request
                .andExpect(MockMvcResultMatchers.content().string("Password must be at least 6 characters long")) // 응답 내용을 문자열로 비교
                .andDo(print()); //요청과 응답 내용을 콘솔에 표기한다. -> 디버깅용

    }
    @Test //유효하지 않은 이메일 입력
    void 회원가입_테스트3()throws Exception{
        MemberRequest request = MemberRequest.builder()
                .username("testUser")
                .password("test1225^^")
                .email("testtest.com") //이메일은 validation이 걸리면 반드시 @와 그 뒤엔 도메인 명이 와야 하기에 이 예시는 오류예시가 맞다.
                .birth(LocalDate.of(1990, 1, 1))
                .gender("male")
                .build();

        // When & Then: 이메일 유효성 검사 실패
        mockMvc.perform(MockMvcRequestBuilders.post("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) //writeValueAsString는 objectMapper의 메서드인데, 객체 변환 실패시 오류를 발생하기에 필히 예외처리를 해줘야 한다.
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Email should be valid")) // 응답 내용을 문자열로 비교
                .andDo(print());
    }
    @Test//유효하지 않은 입력 - username에 blank가 포함된 입력
    void 회원가입_테스트4() throws Exception{

        MemberRequest request = MemberRequest.builder()
                .username("")
                .password("test1225^^")
                .email("test2024@naver.com")
                .birth(LocalDate.of(1990, 1, 1))
                .gender("male")
                .build();

        // When & Then: 이메일 유효성 검사 실패
        mockMvc.perform(MockMvcRequestBuilders.post("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) //writeValueAsString는 objectMapper의 메서드인데, 객체 변환 실패시 오류를 발생하기에 필히 예외처리를 해줘야 한다.
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Username is required")) // 응답 내용을 문자열로 비교
                .andDo(print());
    }
    @Test //예외처리 테스트 - 이미 사용된 이력이 있는 email 사용으로 이메일 중복 테스트
    void 회원가입_테스트5() throws Exception{

        MemberRequest request1 = MemberRequest.builder()
                .username("testUser1")
                .password("test2024^^")
                .email("test2024@naver.com")
                .birth(LocalDate.of(1990, 1, 1))
                .gender("male")
                .build();

        MemberRequest request2 = MemberRequest.builder()
                .username("test2")
                .password("test1225^^")
                .email("test2024@naver.com")
                .birth(LocalDate.of(1991, 11, 11))
                .gender("male")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1))) //writeValueAsString는 objectMapper의 메서드인데, 객체 변환 실패시 오류를 발생하기에 필히 예외처리를 해줘야 한다.
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2))) //writeValueAsString는 objectMapper의 메서드인데, 객체 변환 실패시 오류를 발생하기에 필히 예외처리를 해줘야 한다.
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string("이미 존재하는 이메일입니다."))
                .andDo(print());

    }
    @Test //의존성 모킹 검증
    void 회원가입_테스트6(){

    }

    /* <<회원 조희 테스트 시나리오>> */
    //Mock 정의 : 토큰 추출 메서드 정의,유효성 검사 메서드 정의,이름 추출 메서드 정의

//    1.정상 회원 조회 -정상 조회 확인
//    2.빈 토큰 요청 -에러 정상 출력 확인
//    3.null 값 토큰 요청  - 에러 정상 출력 확인
//    4.헤더가 bearer  가 아닌 경우 -null값 반환
//    5.헤더가 널값 인 경우 - null 값 반환
//    6.의존성 모킹 검증 -jwt 메서드 3개 + memberService 메서드 1개 검증

    @Test //정상 회원 조회
    void 회원조회_테스트1() {}
    @Test //빈 토큰 요청
    void 회원조회_테스트2() {}
    @Test //null 값 토큰 요청
    void 회원조회_테스트3() {}
    @Test //헤더가 bearer가 아닌 경우
    void 회원조회_테스트4() {}
    @Test //헤더가 null값인 경우
    void 회원조회_테스트5() {}
    @Test //의존성 모킹 검증
    void 회원조회_테스트6() {}

    @Test
    void 모든_회원조회_테스트() {
    }

    @Test
    void 회원정보수정_테스트() {
    }

    @Test
    void 회원삭제_테스트() {
    }
}