package MentalCare.ChatBot.domain.Diary.Service;

import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
@RequiredArgsConstructor
public class DiaryUtil {

    public String extractMessageFromJson(String text){

        String message;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(text);
            message = rootNode.get("message").asText();
        } catch (Exception e) {
            return "error";
        }

        return message;
    }

    public LocalDate dateParser(String date){

        LocalDate diaryDate;
        try {
            diaryDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return null;
        }

        return diaryDate;
    }

    public String returnURL(LocalDate diaryDate, Diary diary){

        String url;
        if(diaryDate.equals(LocalDate.now())){
            url = diary.getTemporaryURL();
        }else{
            url = diary.getComicURL();
        }
        return url;
    }

}
