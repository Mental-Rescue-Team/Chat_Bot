package MentalCare.ChatBot.domain.Diary.Service;

import MentalCare.ChatBot.domain.Diary.DTO.Request.DiaryRequest;
import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Diary.Repository.DiaryRepository;
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

    /*일기 요약 메서드 */
    @Override
    public String SummarizeDiary(DiaryRequest diaryRequest) {
        String message = diaryRequest.toString();
        String prompt = "다음 일기를 3줄 또는 4줄로 요약해 주세요."; //prompt message
        String fullMessage = prompt + message;
        return chatClient.call(fullMessage);
    }

    // TODO : 4컷 만화 생성 후 어떻게 저장하고 전송할지 확인
    /*4칸 만화 생성 메서드 */
    @Override
    public String DrawComic(DiaryRequest diaryRequest) {
        String message = diaryRequest.toString();
        String prompt = "다음 일기를 분석하여 재미있는 4칸짜리 만화를 그려줘";
        String fullMessage = prompt + message;
        if (StringUtils.isEmpty(message)) {throw new DiaryException(ErrorCode.EMPTY_DIARY_CONTENT);}
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
    @Override
    public String SaveDiary(DiaryRequest diaryRequest) {
        /*실제 저장은 하지 않고, String type으로 변환후 return*/
        return diaryRequest.toString();
    }

    /*감정 분류 메서드 - 우선 fast-api 의 gpt 에게 맡김*/
    @Override
    public String ClassifyEmotion(DiaryRequest diaryRequest) {
        return "";
    }

    /*감정에 따른 날씨 매칭 메서드 */
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
