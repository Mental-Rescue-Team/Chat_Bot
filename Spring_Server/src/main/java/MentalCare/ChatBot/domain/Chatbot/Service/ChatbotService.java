package MentalCare.ChatBot.domain.Chatbot.Service;

import java.util.List;

public interface ChatbotService {

    /**
     * 챗봇 모드 : 일반 상담사
     * @param username
     * @param message
     * @return 답변 메시지
     */
    String counselorChatting(String username, String message);

    /**
     * 챗봇 모드 : 친근한 친구 모드
     * @param username
     * @param message
     * @return 답변 메시지
     */
    String gptChatting(String username,String message);

    /**
     * 챗봇 모드 : MBTI-T 모드
     * @param username
     * @param message
     * @return 답변 메시지
     */
    String MBTI_T_Chatting(String username,String message);

    /**
     * 챗봇 모드 : MBTI-F 모드
     * @param username
     * @param message
     * @return 답변 메시지
     */
    String MBTI_F_Chatting(String username,String message);

    /**
     * 채팅 종료
     * @param username
     * @return AI 레포트 반환
     */
    List<String> finishChatting(String username);


}
