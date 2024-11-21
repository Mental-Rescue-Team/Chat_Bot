package MentalCare.ChatBot.domain.Chatbot.Service;

import MentalCare.ChatBot.domain.Member.Entity.Member;

import java.util.List;
import java.util.Map;

public interface AiReportService {

    /* AI 레포트 생산 - 3개의 질문 */
    String[] report(List<String> everyMessage);

    /* 사용자의 감정 추출 */
    String getEmotion(String text);

    /* 추천 유튜브 링크 전송 -2개 랜덤 */
    Map<String, String>[] getRandomLink(String emotion);

    /* 레포트와 비디오 저장 */
    void saveReport(Member member, String currentDifficulty, String currentEmotion,
                    String aiAdvice, String video_link1, String video_link2);
}
