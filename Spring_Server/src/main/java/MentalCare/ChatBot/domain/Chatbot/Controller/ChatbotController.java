package MentalCare.ChatBot.domain.Chatbot.Controller;

import MentalCare.ChatBot.domain.Chatbot.Service.AiReportService;
import MentalCare.ChatBot.domain.Chatbot.Service.ChatbotService;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "ChatBot", description = "챗봇 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatbotController {

    private final ChatbotService chatbotService;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final ChatClient chatClient;
    private final AiReportService aiReportService;

    /* GPT 모델 */
    /*친근한 친구 모드*/
    @Operation(summary = " 챗봇 API - 친근한 친구 모드 ", description = " 채팅 화면에서 전송 버튼을 누르면 호출되는 API이다. GPT 챗봇 기능을 한다. ")
    @PostMapping("/chatbot/friend")
    public String GptChatBot(@RequestBody String message, HttpServletRequest request){

        String userToken =jwtUtil.extractTokenFromRequest(request);
        jwtUtil.validateToken_isTokenValid(userToken);
        String username = jwtUtil.extractUsername(userToken);System.out.println("username : " +username);

        return chatbotService.gptChatting(username,message);
    }


    /* GPT 채팅 종료 + AI 레포트 생성 */
    @Operation(summary = "채팅 종료 + AI 레포트 생성 API", description =" 채팅화면에서 채팅 종료 버튼을 누르면 호출되는 API 이다. 채팅을 종료함과 동시에 AI 레포트를 생성해준다")
    @GetMapping("/chatbot/friend/finish")
    public Map<String, Object> finishGpt(HttpServletRequest request){

        Map<String, Object> response = new LinkedHashMap<>(); //순서가 보장이 되는 LinkedHashMap<> 자료구조를 선택

        String userToken =jwtUtil.extractTokenFromRequest(request);
        jwtUtil.validateToken_isTokenValid(userToken);
        String username = jwtUtil.extractUsername(userToken);                   System.out.println("username : " + username);
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()-> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        List<String> everyMessage= chatbotService.finishChatting(username);
        String[] reportResult = aiReportService.report(everyMessage); //GPT에게 AI 레포트 생성 요청하기
        String emotion =  aiReportService.getEmotion(reportResult[1]); // 피상담자의 현재 감정을 추출

        String currentDifficulty = reportResult[0];                             System.out.println(currentDifficulty);
        String currentEmotion = reportResult[1] ;                               System.out.println(currentEmotion);
        String aiAdvice = reportResult[2];                                      System.out.println(aiAdvice);
        String[] videoLinks =aiReportService.getRandomLink(emotion);
        String video_link1= videoLinks[0];                                      System.out.println(video_link1);
        String video_link2= videoLinks[1];                                      System.out.println(video_link2);

        // 실제 DB에 저장 (레포트 + 비디오 링크)
        aiReportService.saveReport( member, currentDifficulty, currentEmotion, aiAdvice ,video_link1,video_link2);

        //Map 형태로 수정하여 클라이언트 측에서 보기 좋게 반환한다.
        response.put("currentDifficulty", currentDifficulty);
        response.put("currentEmotion", currentEmotion);
        response.put("aiAdvice", aiAdvice);
        response.put("video_link1", video_link1);
        response.put("video_link2", video_link2);

        return response;
    }





    /* develope later below */

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

}
