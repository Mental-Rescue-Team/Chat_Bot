package MentalCare.ChatBot.domain.Chatbot.Controller;

import MentalCare.ChatBot.domain.Chatbot.Service.AiReportService;
import MentalCare.ChatBot.domain.Chatbot.Service.ChatbotService;
import MentalCare.ChatBot.domain.Chatbot.Service.ChattingMemory;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "ChatBot", description = "챗봇 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final AiReportService aiReportService;
    private final ChattingMemory chattingMemory;

    /**
     * 챗봇 - 일반 상담사 모드
     * @param message 사용자 메시지
     * @param request 챗봇 답변
     * @return 챗봇 답변
     */
    @Operation(summary = " 챗봇 - 일반 상담사 모드 ", description = " 채팅 화면에서 전송 버튼을 누르면 호출되는 API이다. 일반 상담사 모드를 지원한다.")
    @PostMapping("/counselor")
    public String counselorChatBot(@RequestBody String message , HttpServletRequest request ){

        String username = jwtUtil.extractNameByRequest(request);
        log.info(" Counselor & {}",username);

        return chatbotService.counselorChatting(username, message);
    }

    /**
     * 챗봇 - 친근한 친구 모드
     * @param message 사용자 메시지
     * @param request 챗봇 답변
     * @return 챗봇 답변
     */
    @Operation(summary = " 챗봇 - 친근한 친구 모드 ", description = " 채팅 화면에서 전송 버튼을 누르면 호출되는 API이다. GPT 챗봇 기능을 한다. ")
    @PostMapping("/friend")
    public String friendChatBot(@RequestBody String message, HttpServletRequest request){

        String username = jwtUtil.extractNameByRequest(request);
        log.info(" Friend & {}",username);

        return chatbotService.gptChatting(username,message);
    }

    /**
     * 챗봇 - MBTI - T 모드
     * @param message 사용자 메시지
     * @param request 챗봇 답변
     * @return 챗봇 답변
     */
    @Operation(summary = " 챗봇 - MBTI - T 모드 ", description = " 채팅 화면에서 전송 버튼을 누르면 호출되는 API이다. GPT 챗봇 기능을 한다. ")
    @PostMapping("/T")
    public String MBTI_T_ChatBot(@RequestBody String message, HttpServletRequest request){

        String username = jwtUtil.extractNameByRequest(request);
        log.info(" MBTI-T & {}",username);

        return chatbotService.MBTI_T_Chatting(username,message);
    }

    /**
     * 챗봇 - MBTI - F 모드
     * @param message 사용자 메시지
     * @param request 챗봇 답변
     * @return 챗봇 답변
     */
    @Operation(summary = " 챗봇 - MBTI - F 모드 ", description = " 채팅 화면에서 전송 버튼을 누르면 호출되는 API이다. GPT 챗봇 기능을 한다. ")
    @PostMapping("/F")
    public String MBTI_F_ChatBot(@RequestBody String message, HttpServletRequest request){

        String username = jwtUtil.extractNameByRequest(request);
        log.info(" MBTI-F & {}",username);

        return chatbotService.MBTI_F_Chatting(username,message);
    }

    /**
     * AI 레포트 생성
     * @param request 사용자 요청
     * @return AI 레포트 반환
     */
    @Operation(summary = "채팅 종료 + AI 레포트 생성 API", description =" 채팅화면에서 채팅 종료 버튼을 누르면 호출되는 API 이다. 채팅을 종료함과 동시에 AI 레포트를 생성해준다")
    @GetMapping("/finish")
    public Map<String, Object> finishFriendChatBot(HttpServletRequest request){

        Map<String, Object> response = new LinkedHashMap<>(); //순서가 보장이 되는 LinkedHashMap<> 자료구조를 선택

        Member member = jwtUtil.extractMemberByRequest(request);

        /*
        * 1. 메모리에서 모든 메시지 호출
        * 2. 메모리 초기화
        * 3. GPT에게 AI 레포트 생성 요청
        * 4. 사용자의 현재 감정을 추출
        */

        List<String> everyMessage= chatbotService.finishChatting(member.getUsername());
        chattingMemory.clearMessages(member.getUsername());
        String[] reportResult = aiReportService.report(everyMessage);
        String emotion =  aiReportService.getEmotion(reportResult[1]);

        /*
        * 1. AI 레포트 생성
        * 2. 감정 분류 (GPT)
        * 3. 감정 값에 맞추어 비디오 링크 2개 랜덤 반환
        */

        String currentDifficulty = reportResult[0];
        String currentEmotion = reportResult[1] ;
        String aiAdvice = reportResult[2];

        Map<String, String>[] videoLinks =aiReportService.getRandomLink(emotion);

        String title1 =null;
        String link1 = null;
        String title2 =null;
        String link2 =null;

        Map<String, String> videoMap0 = videoLinks[0];
        for (Map.Entry<String, String> entry : videoMap0.entrySet()) {
            title1 = entry.getKey();   // 키 값
            link1 = entry.getValue(); // 값 값
        }

        Map<String, String> videoMap1 = videoLinks[1];
        for (Map.Entry<String, String> entry : videoMap1.entrySet()) {
            title2 = entry.getKey();   // 키 값
            link2 = entry.getValue(); // 값 값
        }

        // 실제 DB에 저장 (레포트 + 비디오 링크)
        aiReportService.saveReport( member, currentDifficulty, currentEmotion, aiAdvice ,link1,link2);

        //Map 형태로 수정하여 클라이언트 측에서 보기 좋게 반환한다.
        response.put("currentDifficulty", currentDifficulty);
        response.put("currentEmotion", currentEmotion);
        response.put("aiAdvice", aiAdvice);
        response.put("title1",title1);
        response.put("video_link1", link1);
        response.put("title2",title2);
        response.put("video_link2", link2);

        return response;
    }

}
