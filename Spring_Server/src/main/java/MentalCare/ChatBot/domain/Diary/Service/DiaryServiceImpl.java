package MentalCare.ChatBot.domain.Diary.Service;

import MentalCare.ChatBot.domain.Diary.DTO.Response.DateEmoji;
import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Diary.Repository.DiaryRepository;
import MentalCare.ChatBot.domain.FastAPIConnection.Client.ApiClient;
import MentalCare.ChatBot.domain.Member.Entity.Member;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final ChatClient chatClient;
    private final ImageClient imageClient;
    private final ApiClient apiClient;

    /*일기 요약 메서드 */
    @Override
    public String SummarizeDiary(String text) {
        //String message = diaryRequest.toString();
        String prompt = "다음 일기를 1줄 또는 2줄로 요약해 줘->"; //prompt message
        String fullMessage = prompt + text;
        return chatClient.call(fullMessage);
    }

    // TODO : 4컷 만화 생성 후 어떻게 저장하고 전송할지 확인
    /*4칸 만화 생성 메서드 */
    @Override
    public String DrawComic(String text ,String gender) {
        String prompt = " 이 일기의 당사자의 성별은 "+  gender +" 이다. \n "
                + "이어지는 일기를 분석하여 이 일기 내용을 요약하는 재미있는 4칸짜리 만화를 그려주되,"
                + "만화를 그려줄때는 색깔을 좀 넣어서 보기 좋게 만들어 주고, 만화에 절대 글자는 넣지 말아줘, \n"
                + "만화의 주인공의 성별은 " +gender + "로 그려라. \n "
                + "사용자의 일기는 다음과 같다 : ";
        String fullMessage = prompt + text;
        if (StringUtils.isEmpty(text)) {throw new DiaryException(ErrorCode.EMPTY_DIARY_CONTENT);}
        OpenAiImageOptions imageOptions = OpenAiImageOptions.builder()
                .withQuality("standard")
                .withHeight(1024)
                .withN(1)
                .withWidth(1024)
                .build();
        ImagePrompt imagePrompt = new ImagePrompt(fullMessage, imageOptions);
        Image img = imageClient.call(imagePrompt).getResult().getOutput();
        return img.getUrl();
    }

    @Override
    public String SaveDiary(String text) {
        return text;
    }

    // TODO : fast-api의 코드로 변환하기
    /*감정 분류 메서드 - 우선 fast-api 의 gpt 에게 맡김*/
    @Override
    public String ClassifyEmotion(String text) {
        //String message = diaryRequest.toString();
        String prompt ="이 일기를 (기쁨,슬픔, 평온, 분노, 불안) 중 하나의 감정으로 감정을 분류해줘. 대답할때는 기쁨,슬픔 이렇게 두 글자의 단어로 답변을 해줘.-> ";
        String fullMessage = prompt + text;
        return apiClient.sendData(fullMessage);
    }

    public String extractEmotion(String response) {
        // 감정 리스트 정의
        String[] emotions = {"기쁨", "슬픔", "평온", "분노", "불안"};

        // 감정 추출
        for (String emotion : emotions) {
            if (response.contains(emotion)) {
                return emotion;  // 감정을 찾으면 해당 감정 반환
            }
        }

        // 감정이 없을 경우 null 반환
        return null;
    }

    /*감정에 따른 날씨 매칭 메서드 */
    @Override
    public Map<String, String> WeatherMatch(String diaryEmotion) {

        /* 계속 diaryEmotion에 [ 과 ] 가 붙어서 와서 매칭이 안된다, 그래서 제거하는 로직을 추가함 */
        //String cleanEmotion = diaryEmotion.replaceAll("\\[|\\]|\"", "").trim();
        String cleanEmotion = extractEmotion(diaryEmotion);
        String weather;
        String weatherEmoji;

        switch (cleanEmotion) {
            case "기쁨" -> {weather = "Sunny";weatherEmoji = "☀️";}
            case "슬픔" -> {weather = "Rainy";weatherEmoji = "🌧️";}
            case "분노" -> {weather = "Stormy";weatherEmoji = "🌩️";}
            case "평온" -> {weather = "Cloudy";weatherEmoji = "☁️";}
            case "불안" -> {weather = "Windy";weatherEmoji = "🌬️";}
            default -> {weather = "Unknown";weatherEmoji = "❓";}
        }

        return Map.of("weather", weather, "weatherEmoji", weatherEmoji);
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


    // FIXME : 이모지가 아닌 날씨 이름 보내주기 -완료

    @Override
    public List<DateEmoji> getEveryDateEmoji(int month, Member member) {

        List<Diary> diaries = diaryRepository.findByMember(member);

        return diaries.stream()
                .filter(diary -> diary.getDiaryDate().getMonthValue() == month) // 특정 월 필터링
                .map(diary -> new DateEmoji(diary.getDiaryDate(), diary.getWeather())) // DateEmoji로 변환
                .collect(Collectors.toList());
    }

    @Override
    public String getMemberEmotion(Member member) {

        return diaryRepository.findByMemberAndDiaryDate(member,LocalDate.now())
                .map(Diary::getDiaryEmotion)
                .orElse("[\"아직 오늘의 감정이 없음\"]");
    }

    @Override
    public Map<String, Long> getEmotionCounts() {
        // 기본 감정 리스트를 정의하고, 각 감정을 0으로 초기화
        Map<String, Long> emotionCounts = new HashMap<>();
        List<String> defaultEmotions = List.of("[\"기쁨\"]", "[\"평온\"]", "[\"분노\"]", "[\"슬픔\"]", "[\"불안\"]");

        for (String emotion : defaultEmotions) {
            emotionCounts.put(emotion, 0L);
        }

        // DB에서 가져온 결과를 emotionCounts에 업데이트
        List<Object[]> results = diaryRepository.countDiariesByEmotion();
        for (Object[] result : results) {
            String emotion = (String) result[0];
            Long count = (Long) result[1];
            emotionCounts.put(emotion, count);
        }

        return emotionCounts;
    }
}

