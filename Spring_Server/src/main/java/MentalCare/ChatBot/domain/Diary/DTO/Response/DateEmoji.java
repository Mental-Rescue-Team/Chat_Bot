package MentalCare.ChatBot.domain.Diary.DTO.Response;

import java.time.LocalDate;

public record DateEmoji(LocalDate diaryDate, String weatherEmoji) {
}
