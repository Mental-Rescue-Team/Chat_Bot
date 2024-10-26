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

    /* GPT 챗봇 채팅 */
    /*친근한 친구 모드*/
    @Override
    public String gptChatting(String username, String message) {

        // Redis에서 이전 메시지 가져오기
        List<String> previousMessages = redisTemplate.opsForList().range(username + ":chat", 0, -1);

        // 이전 메시지를 간략히 정리하여 문자열로 변환
        StringBuilder previousMessagesSummary = new StringBuilder();
        if (previousMessages != null && !previousMessages.isEmpty()) {
            // 이전 메시지를 최신 것부터 4개만 보여주기
            int count = Math.min(previousMessages.size(), 4);
            for (int i = previousMessages.size() - count; i < previousMessages.size(); i++) {
                previousMessagesSummary.append(previousMessages.get(i)).append("\n");
            }
        }
        System.out.println("이전의 대화 내용 = " + previousMessagesSummary.toString());

        String prompt = "당신은 멘탈케어 어플리케이션의 AI 챗봇 상담사입니다."+
                "당신의 상담모드는 친근한 친구 모드입니다."
                +"사용자가 편안하게 이야기할 수 있는 친근한 친구 같은 캐릭터로, 비공식적인 언어로 소통하며 감정을 나눌 수 있게 돕습니다."
                +"이전 대화 내용:\n" + previousMessagesSummary.toString()
                +"지금부터 위 조건에 맞게 다음 사용자의 메시지에 적절하게 답변해주세요 ->";
        String fullMessage = prompt + message;
        String response = chatClient.call(fullMessage);
        System.out.println("message = " + message);
        System.out.println("response = " + response);

        // Redis에 사용자와 챗봇의 메시지 저장
        saveMessage(username, message, "user");
        saveMessage(username, response, "chatbot");

        return response;
    }

    /* Redis에 메시지 저장 */
    // 데이터는 List형식으로 저장이 된다.
    private void saveMessage(String username, String message, String sender) {
        String key = username + ":chat";
        String taggedMessage = sender + ": " + message;
        redisTemplate.opsForList().rightPush(key, taggedMessage);
    }

    /* GPT 채팅 종료 - Redis에서 대화 내용 가져오기 */
    /* GPT 채팅 종료 */
    @Override
    public List<String> finishChatting(Long member_no) {
        String key = member_no + ":chat";
        return redisTemplate.opsForList().range(key, 0, -1);  // 저장된 모든 대화 내용 반환
    }


}
