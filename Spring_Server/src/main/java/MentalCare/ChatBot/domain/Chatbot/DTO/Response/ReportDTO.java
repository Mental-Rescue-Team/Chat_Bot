package MentalCare.ChatBot.domain.Chatbot.DTO.Response;

public record ReportDTO(
        Long report_no,
        String currentDifficulty,
        String currentEmotion,
        String aiAdvice
) {
}
