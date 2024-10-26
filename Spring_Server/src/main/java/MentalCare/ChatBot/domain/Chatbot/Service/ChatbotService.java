package MentalCare.ChatBot.domain.Chatbot.Service;

import java.util.List;

public interface ChatbotService {

    /* GPT 챗봇 채팅 */
    String gptChatting(String username,String message);

    /* GPT 채팅 종료 */
    List<String> finishChatting(Long member_no);

}
