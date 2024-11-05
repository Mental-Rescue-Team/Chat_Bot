package MentalCare.ChatBot.domain.Chatbot.Service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChattingMemory {
    private final ConcurrentHashMap<String, List<String>> chatMessagesMap = new ConcurrentHashMap<>();

    /* ConcurrentHashMap에 메시지 저장 */
    public void saveMessage(String username, String message, String sender) {
        // 사용자별로 메시지 리스트를 얻거나 새로 만듦
        chatMessagesMap.computeIfAbsent(username, key -> new ArrayList<>());

        // 해당 사용자의 메시지 리스트에 새로운 메시지 추가
        String taggedMessage = sender + ": " + message;
        chatMessagesMap.get(username).add(taggedMessage);
    }
    /* 사용자별로 메시지 리스트를 가져오거나 기본값으로 빈 리스트 반환 */
    public List<String> getOrDefault(String username) {
        // username에 해당하는 메시지 리스트가 없으면 빈 리스트 반환
        return chatMessagesMap.getOrDefault(username, new ArrayList<>());
    }
}
