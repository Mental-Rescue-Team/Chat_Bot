package MentalCare.ChatBot.domain.Chatbot.Service;

import MentalCare.ChatBot.domain.FastAPIConnection.Client.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService{

    private final ChatClient chatClient;
    private final RedisTemplate<String, String> redisTemplate;
    private final ChatBotUtil chatBotUtil;
    private final ChattingMemory chattingMemory;
    private final ApiClient apiClient;

    /* 채팅 모드 - 일반 상담사 모드  */
    @Override
    public String counselorChatting(String username, String message) {

        String userMessage = chatBotUtil.extractUserMessageFromJson(message);//Json에서 메시지 파싱
        StringBuilder previousMessagesSummary =chatBotUtil.getPreviousMessagesSummary(username);//이전의 메시지들중 4개 까지만 받아오기
        chatBotUtil.getLogFromFourMessageFromRedis(previousMessagesSummary);//이전 4개의 메시지 로그 확인
        String rawResponse = apiClient.sendData(message);
        String response = chatBotUtil.extractChatResponse(rawResponse);// 문제의 문자열 정제 로직
        chatBotUtil.getLogFromGeneratedMessage(userMessage,response); //이번에 도출된 메시지 로그 확인
        chatBotUtil.saveMessageInMap(username,userMessage,response); //메모리에 이번 메시지 저장

        return response;
    }

    /* 채팅 모드 - 친근한 친구 모드 */
    @Override
    public String gptChatting(String username, String message) {

        String userMessage = chatBotUtil.extractUserMessageFromJson(message);//Json에서 메시지 파싱
        StringBuilder previousMessagesSummary =chatBotUtil.getPreviousMessagesSummary(username);//이전의 메시지들중 4개 까지만 받아오기
        chatBotUtil.getLogFromFourMessageFromRedis(previousMessagesSummary);//이전 4개의 메시지 로그 확인
        String prompt = chatBotUtil.getPromptMessage("친근한 친구 모드",previousMessagesSummary); //모드에 따른 프롬프팅
        String fullMessage = prompt + userMessage;
        String rawResponse = chatClient.call(fullMessage);
        String response = chatBotUtil.extractChatResponse(rawResponse); // 문제의 문자열 정제 로직
        chatBotUtil.getLogFromGeneratedMessage(userMessage,response); //이번에 도출된 메시지 로그 확인
        chatBotUtil.saveMessageInMap(username,userMessage,response); //메모리에 이번 메시지 저장

        return response;
    }

    /* 채팅 모드 - MBTI - T 모드 */
    @Override
    public String MBTI_T_Chatting(String username, String message) {

        String userMessage = chatBotUtil.extractUserMessageFromJson(message);//Json에서 메시지 파싱
        StringBuilder previousMessagesSummary =chatBotUtil.getPreviousMessagesSummary(username);//이전의 메시지들중 4개 까지만 받아오기
        chatBotUtil.getLogFromFourMessageFromRedis(previousMessagesSummary);//이전 4개의 메시지 로그 확인
        String prompt = chatBotUtil.getPromptMessage("T 모드",previousMessagesSummary); //모드에 따른 프롬프팅
        String fullMessage = prompt + userMessage;
        String rawResponse = chatClient.call(fullMessage);
        String response = chatBotUtil.extractChatResponse(rawResponse); // 문제의 문자열 정제 로직
        chatBotUtil.getLogFromGeneratedMessage(userMessage,response); //이번에 도출된 메시지 로그 확인
        chatBotUtil.saveMessageInMap(username,userMessage,response); //메모리에 이번 메시지 저장

        return response;
    }

    /* 채팅 모드 - MBTI - F 모드 */
    @Override
    public String MBTI_F_Chatting(String username, String message) {

        String userMessage = chatBotUtil.extractUserMessageFromJson(message);//Json에서 메시지 파싱
        StringBuilder previousMessagesSummary =chatBotUtil.getPreviousMessagesSummary(username);//이전의 메시지들중 4개 까지만 받아오기
        chatBotUtil.getLogFromFourMessageFromRedis(previousMessagesSummary);//이전 4개의 메시지 로그 확인
        String prompt = chatBotUtil.getPromptMessage("F 모드",previousMessagesSummary); //모드에 따른 프롬프팅
        String fullMessage = prompt + userMessage;
        String rawResponse = chatClient.call(fullMessage);
        String response = chatBotUtil.extractChatResponse(rawResponse); // 문제의 문자열 정제 로직
        chatBotUtil.getLogFromGeneratedMessage(userMessage,response); //이번에 도출된 메시지 로그 확인
        chatBotUtil.saveMessageInMap(username,userMessage,response); //메모리에 이번 메시지 저장

        return response;
    }


    /* GPT 채팅 종료 + ConcurrentHashMap에서 대화 내용 가져오기 */
    public List<String> finishChatting(String username) {
        // ConcurrentHashMap에서 사용자의 대화 내용 가져오기
        return chattingMemory.getOrDefault(username);
    }

//    /* GPT 채팅 종료 + Redis에서 대화 내용 가져오기 */
//    @Override
//    public List<String> finishChatting(String username) {
//        String key = username + ":chat";
//        return redisTemplate.opsForList().range(key, 0, -1);  // 저장된 모든 대화 내용 반환
//    }

}
