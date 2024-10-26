package MentalCare.ChatBot.domain.Chatbot.Controller;

import MentalCare.ChatBot.domain.Chatbot.Service.ChatbotService;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ChatBot", description = "챗봇 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatbotController {

    private final ChatbotService chatbotService;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final ChatClient chatClient;

    /* GPT 모델 */
    /*친근한 친구 모드*/
    @Operation(summary = " GPT 챗봇 API ", description = " GPT 챗봇 기능")
    @PostMapping("/chatbot/friend")
    public String GptChatBot(@RequestBody String message, HttpServletRequest request){

        String userToken =jwtUtil.extractTokenFromRequest(request);
        jwtUtil.validateToken_isTokenValid(userToken);
        String username = jwtUtil.extractUsername(userToken);System.out.println("username : " +username);

        return chatbotService.gptChatting(username,message);
    }

    /* GPT 채팅 종료 */
    /*친근한 친구 모드*/
    //@Operation(summary = "채팅 종류 API", description =" ")
    @GetMapping("/chatbot/finish/friend")
    public List<String> finishGpt(Long member_no){

        //모든 메시지 가져오기
        List<String> everyMessage= chatbotService.finishChatting(member_no);

        //GPT에게 AI 레포트 생성 요청하기

        return null;
    }






    /* BERT 모델 */
    //@Operation(summary = " BERT 챗봇 API ", description = " BERT 챗봇 기능")
    @PostMapping("/chatbot/bert")
    public String BertChatBot(@RequestBody String message){

        // 메시지를 BERT에게 전송
        // 요청을 클라이언트에게 전송
        return null;
    }
    /* BERT 채팅 종료 */
    //@Operation(summary = "채팅 종류 API", description =" ")
    @GetMapping("/chatbot/finish/bert")
    public List<String> finishBert(){

        return null;
    }


    /* Test Method Below */

    /* 챗봇 도메인 관련 테스팅 */
    /* 1. API를 사용하여 챗봇을 구현하면, 이전의 대화의 내용을 끌어올 수 있는가? */
    //간단한 API 만들어서 여러번 요청을 보내보기
    // 1번 요청-나는 오늘 샌드위치를 만들어 먹었어
    // 2번 요청-나는 오늘 수박을 후식으로 먹었어
    // 3번 요청-내가 오늘 무엇을 먹었니?
    @PostMapping("/chatbot/test")
    public String chatTest(@RequestBody String message){
        return chatClient.call(message);
    }
    /*테스팅 결과 : GPT는 이전의 대화를 기억하지 못한다. */
}
