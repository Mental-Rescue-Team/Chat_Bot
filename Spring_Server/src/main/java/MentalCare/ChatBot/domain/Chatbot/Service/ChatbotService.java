package MentalCare.ChatBot.domain.Chatbot.Service;

import java.util.List;

public interface ChatbotService {

    /* 상담사 모드 채팅 */
    String counselorChatting(String username, String message);

    /* 친근한 친구 모드 채팅 */
    String gptChatting(String username,String message);

    /* MBTI - T 모드 채팅 */
    String MBTI_T_Chatting(String username,String message);

    /* MBTI - F 모드 채팅 */
    String MBTI_F_Chatting(String username,String message);

    /* GPT 채팅 종료 */
    List<String> finishChatting(String username);


}
