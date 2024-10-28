package MentalCare.ChatBot.domain.Chatbot.Service;

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

    /* 채팅 모드 - 친근한 친구 모드 */
    @Override
    public String gptChatting(String username, String message) {

        String userMessage = chatBotUtil.extractUserMessageFromJson(message);//Json에서 메시지 파싱
        StringBuilder previousMessagesSummary =chatBotUtil.getPreviousMessagesSummary(username);//이전의 메시지들중 4개 까지만 받아오기
        chatBotUtil.getLogFromFourMessageFromRedis(previousMessagesSummary);//이전 4개의 메시지 로그 확인
        String prompt = chatBotUtil.getPromptMessage("친근한 친구 모드",previousMessagesSummary); //모드에 따른 프롬프팅
        String fullMessage = prompt + userMessage;
        String response = chatClient.call(fullMessage);
        chatBotUtil.getLogFromGeneratedMessage(userMessage,response); //이번에 도출된 메시지 로그 확인
        chatBotUtil.saveMessageInRedis(username,userMessage,response); //Redis 메모디에 이번 메시지 저장

        return response;
    }

    /* GPT 채팅 종료 + Redis에서 대화 내용 가져오기 */
    @Override
    public List<String> finishChatting(String username) {
        String key = username + ":chat";
        return redisTemplate.opsForList().range(key, 0, -1);  // 저장된 모든 대화 내용 반환
    }

}
