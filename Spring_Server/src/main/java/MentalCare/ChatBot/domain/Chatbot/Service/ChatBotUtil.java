package MentalCare.ChatBot.domain.Chatbot.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatBotUtil {

    private final RedisTemplate<String, String> redisTemplate;
    private final ChattingMemory chattingMemory;

    /* 이전의 대화 내용 로그 출력 */
    public void getLogFromFourMessageFromRedis(StringBuilder previousMessagesSummary){
        System.out.println("--- 이전의 대화 내용(최대4개) ---" );
        System.out.println( previousMessagesSummary.toString());
    }

    /* 이번에 도출된 메시지 로그 출력 */
    public void getLogFromGeneratedMessage(String userMessage ,String response){
        System.out.println("---이번에 도출된 메시지---");
        System.out.println("message = " + userMessage);
        System.out.println("response = " + response);
    }

    /* Prompt Message - 선택한 모드에 따라 프롬프트 메시지 변환*/
    public String getPromptMessage(String mood, StringBuilder previousMessagesSummary){
        return switch (mood) {

            case "친근한 친구 모드" ->
                    " 당신은 멘탈케어 어플리케이션의 AI 챗봇 상담사입니다." +
                            " 당신의 상담 모드는 친근한 친구 모드입니다." +
                            " 사용자가 편안하게 이야기할 수 있는 친근한 친구 같은 캐릭터로, 비공식적인 언어로 소통하며 감정을 나눌 수 있게 돕습니다." +
                            " 이전 대화 내용:\\n" + previousMessagesSummary.toString() +
                            " 지금부터 위 조건에 맞게 다음 사용자의 메시지에 적절하게 답변해주세요 ->";

            case "T 모드" ->
                    " 당신은 멘탈케어 어플리케이션의 AI 챗봇 상담사입니다." +
                            " 당신의 상담 모드는 MBTI -T 모드입니다." +
                            " 당신의 말투는 다음과 같습니다 : { 까칠함, 직설적임 , 단호함} " +
                            " 당신의 특징은 다음과 같습니다 : { 공감보다는 상대방의 현재 상황과 문제에 대한 해결책과 조언을 직설적으로 표현한다.} " +
                            " 추가적으로 당신은 부드러운 이모티콘과 말투는 사용하지 않는 챗봇 상담사입니다" +
                            " 이전 대화 내용:\\n" + previousMessagesSummary.toString() +
                            " 지금부터 위 조건에 맞게 다음 사용자의 메시지에 적절하게 답변해주세요 ->";

            case "F 모드" ->
                    " 당신은 멘탈케어 어플리케이션의 AI 챗봇 상담사입니다." +
                            " 당신의 상담 모드는 MBTI -F 모드입니다." +
                            " 당신의 말투는 다음과 같습니다 : { 부드러움, 따뜻함, 공감적임, 포근함 } " +
                            " 당신의 특징은 다음과 같습니다 : { 상대방의 현실의 문제와 상황에 대한 해결책과 조언 보다는 공감과 응원을 하며 상대방과 대화를 이어나간다.} " +
                            " 추가적으로 당신은 부드러운 이모티콘과 말투를 주로 사용하는 챗봇 상담사입니다" +
                            " 이전 대화 내용:\\n" + previousMessagesSummary.toString() +
                            " 지금부터 위 조건에 맞게 다음 사용자의 메시지에 적절하게 답변해주세요 ->";

            default ->
                    " 당신은 멘탈케어 어플리케이션의 AI 챗봇 상담사입니다." +
                            " 당신의 상담 모드는 일반 상담사 모드입니다." +
                            " 당신의 말투는 다음과 같습니다 : { 편안함 , 공감적임 } " +
                            " 당신의 특징은 다음과 같습니다 : { 상대방의 현재 상황을 분석하고, 명확한 조언과 해결책을 추천한다.} " +
                            " 추가적으로 당신은 상대방의 현재의 어려움과 문제를 정확히 파악하게 하고 상대방이 앞으로의 목표를 올바르게 설정하게 하도록 돕는 챗봇 상담사입니다" +
                            " 이전 대화 내용:\\n" + previousMessagesSummary.toString() +
                            " 지금부터 위 조건에 맞게 다음 사용자의 메시지에 적절하게 답변해주세요 ->";

        };
    }

    /* Json 객체 파싱을 위한 ObjectMapper 객체 생성*/
    // JSON에서 "message"를 추출하는 메서드
    public String extractUserMessageFromJson(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        String userMessage = message;  // 초기화

        try {
            // JSON 형식의 String을 Map으로 변환
            Map<String, String> jsonMap = objectMapper.readValue(message, Map.class);
            userMessage = jsonMap.get("message");  // "message" 키의 값 추출
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userMessage;
    }

    /* ConcurrentHashMap에서 이전 메시지 요약 가져오기 */
    public StringBuilder getPreviousMessagesSummary(String username) {
        // ConcurrentHashMap에서 이전 메시지 가져오기
        List<String> previousMessages = chattingMemory.getOrDefault(username);

        // 이전 메시지를 간략히 정리하여 문자열로 변환
        StringBuilder previousMessagesSummary = new StringBuilder();
        if (!previousMessages.isEmpty()) {
            // 이전 메시지를 최신 것부터 4개만 보여주기
            int count = Math.min(previousMessages.size(), 4);
            for (int i = previousMessages.size() - count; i < previousMessages.size(); i++) {
                previousMessagesSummary.append(previousMessages.get(i)).append("\n");
            }
        }

        return previousMessagesSummary;  // 요약된 메시지를 반환
    }

    /* ConcurrentHashMap에 사용자와 챗봇의 메시지 저장 */
    public void saveMessageInMap(String username, String userMessage, String response) {
        // ConcurrentHashMap에 사용자와 챗봇의 메시지 저장
        chattingMemory.saveMessage(username, userMessage, "user");
        chattingMemory.saveMessage(username, response, "chatbot");
    }

    // TODO : 이상한 문자열 정제 system: - assistant: -chatbot: - 시스템:
    public String extractChatResponse(String rawResponse){

        //발견된 "문제의 문자열"리스트
        String[] problematicStrings ={"system: ","chatbot: ","assistant: ","시스템: "};

        //"타깃 문자열"에서 "문제의 문자열"정제
        for(String problematicString : problematicStrings ){
            if(rawResponse.contains(problematicString)){
                //return rawResponse.replace(problematicString,"");
                rawResponse = rawResponse.replace(problematicString, "");
            }
        }
        // '['와 ']' 제거
        rawResponse = rawResponse.replace("[", "").replace("]", "");

        return rawResponse;
    }

    /* Redis Code Below */

//    /* Redis에서 이전 메시지 가져오기 */
//    // 이전 메시지를 요약하여 문자열로 반환하는 메서드
//    public StringBuilder getPreviousMessagesSummary(String username) {
//        // Redis에서 이전 메시지 가져오기
//        List<String> previousMessages = redisTemplate.opsForList().range(username + ":chat", 0, -1);
//
//        // 이전 메시지를 간략히 정리하여 문자열로 변환
//        StringBuilder previousMessagesSummary = new StringBuilder();
//        if (previousMessages != null && !previousMessages.isEmpty()) {
//            // 이전 메시지를 최신 것부터 4개만 보여주기
//            int count = Math.min(previousMessages.size(), 4); // 둘 중 더 작은 값은 반환
//            for (int i = previousMessages.size() - count; i < previousMessages.size(); i++) {
//                previousMessagesSummary.append(previousMessages.get(i)).append("\n");
//            }
//        }
//
//        return previousMessagesSummary;  // 요약된 메시지를 반환
//    }

//    /* Redis에서 사용자와 챗봇의 메시지 저장*/
//    public void saveMessageInRedis(String username,String userMessage ,String response){
//        // Redis에 사용자와 챗봇의 메시지 저장
//        saveMessage(username, userMessage, "user");
//        saveMessage(username, response, "chatbot");
//
//    }
//
//    /* Redis에 메시지 저장 */
//    // 데이터는 List형식으로 저장이 된다.
//    private void saveMessage(String username, String message, String sender) {
//        String key = username + ":chat";
//        String taggedMessage = sender + ": " + message;
//        redisTemplate.opsForList().rightPush(key, taggedMessage);
//    }
}
