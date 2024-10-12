package MentalCare.ChatBot.domain.Diary.Service;

import MentalCare.ChatBot.domain.Diary.DTO.Request.DiaryRequest;
import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Diary.Repository.DiaryRepository;
import MentalCare.ChatBot.domain.FastAPIConnection.Client.ApiClient;
import MentalCare.ChatBot.global.Exception.DiaryException;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final ChatClient chatClient; //일기 요약 + 채팅 전용 객체
    private final ImageClient imageClient; //일기를 통해 4칸 만화 생성 객체
    private final ApiClient apiClient; // 감정 분률 모델 사용시 fast-api로 요청을 보낼 클라이언트

    /*일기 요약 메서드 */
    //실패
    @Override
    public String SummarizeDiary(String text) {
        //String message = diaryRequest.toString();
        String prompt = "다음 일기를 3줄 또는 4줄로 요약해 주세요->"; //prompt message
        String fullMessage = prompt + text;
        return chatClient.call(fullMessage);
    }

    // TODO : 4컷 만화 생성 후 어떻게 저장하고 전송할지 확인
    /*4칸 만화 생성 메서드 */
    //성공
    @Override
    public String DrawComic(String text) {
        //String message = diaryRequest.toString();
        String prompt = "다음 일기를 분석하여 재미있는 4칸짜리 만화를 그려줘->";
        String fullMessage = prompt + text;
        if (StringUtils.isEmpty(text)) {throw new DiaryException(ErrorCode.EMPTY_DIARY_CONTENT);}
        OpenAiImageOptions imageOptions = OpenAiImageOptions.builder()
                .withQuality("standard")
                .withHeight(1024)
                .withN(1)
                .withWidth(1792)
                .build();
        ImagePrompt imagePrompt = new ImagePrompt(fullMessage, imageOptions);
        Image img = imageClient.call(imagePrompt).getResult().getOutput();
        return img.getUrl();
    }

    /*텍스트 DTO 텍스트 String 으로 변환 메서드*/
    //실페
    @Override
    public String SaveDiary(String text) {

        return text;
    }

    /*감정 분류 메서드 - 우선 fast-api 의 gpt 에게 맡김*/
    //성공 다만, response는 빼고 단어만 가져오기
    @Override
    public String ClassifyEmotion(String text) {
        //String message = diaryRequest.toString();
        String prompt ="이 일기를 (기쁨,슬픔, 평안, 분노, 불안) 중 하나의 감정으로 감정을 분류해줘. 대답할때는 대답할때는 기쁨,슬픔 이렇게 단어로 답변을 해줘.-> ";
        String fullmessage = prompt + text;
        return apiClient.sendData(fullmessage);
    }

    /*감정에 따른 날씨 매칭 메서드 */
    //실패 - 아마 responese 단어 때문일듯
    @Override
    public Map<String, String> WeatherMatch(String diaryEmotion) {

        String weather;
        String weatherEmoji;

        switch (diaryEmotion) {
            case "기쁨" -> {weather = "Sunny";weatherEmoji = "☀️";}
            case "슬픔" -> {weather = "Rainy";weatherEmoji = "🌧️";}
            case "분노" -> {weather = "Stormy";weatherEmoji = "🌩️";}
            case "평온" -> {weather = "Cloudy";weatherEmoji = "☁️";}
            case "불안" -> {weather = "Windy";weatherEmoji = "🌬️";}
            default -> {weather = "Unknown";weatherEmoji = "❓";}
        }
        return Map.of(weather,weatherEmoji);
    }

    /*일기 객체 DB에 저장 메서드  */
    @Override
    public void saveDiary(Diary diary) {
        diaryRepository.save(diary);
    }

    /*날짜로 일기 조회 메서드*/
    @Override
    public Diary getDiaryByDate(LocalDate date) {
        return diaryRepository.findByDiaryDate(date);
    }
}
