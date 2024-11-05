package MentalCare.ChatBot.domain.Chatbot.Service;

import MentalCare.ChatBot.domain.Chatbot.Entity.Report;
import MentalCare.ChatBot.domain.Chatbot.Entity.VideoLink;
import MentalCare.ChatBot.domain.Chatbot.Repository.ReportRepository;
import MentalCare.ChatBot.domain.Chatbot.Repository.VideoLinkRepository;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiReportServiceImpl implements AiReportService{

    private final ChatClient chatClient;
    private final VideoLinkRepository videoLinkRepository;
    private final ReportRepository reportRepository;

    /* AI 레포트 생성 */
    @Override
    public String[] report(List<String> everyMessage) {

        String[] result = new String[5];

        String prompt1 = "다음 모든 메시지들(사용자와 챗봇의 대화 내용)을 보고 아래 질문에 1~2문장으로 답을 해줘라"
                +"사용자 현재의 어려움은 무엇인가? ";
        String fullMessage1 =prompt1 + everyMessage;

        String prompt2= "다음 모든 메시지들(사용자와 챗봇의 대화 내용)을 보고 아래 질문에 1~2문장으로 답을 해줘라"
                +"사용자가 오늘 느꼈던 감정은 무엇인가?";
        String fullMessage2= prompt2 + everyMessage;

        String prompt3 ="다음 모든 메시지들(사용자와 챗봇의 대화 내용)을 보고 아래 질문에 1~2문장으로 답을 해줘라 "
                + " 챗봇이 사용자에게 마지막으로 조언해주고 싶은 한마디를 다음과 예시의 말투와 같이 출력하라 "
                +" (예시1)항상 함께 응원해주는 친구가 있으니 너는 힘낼 수 있어. 계속 이야기해주고 함께 화이팅해보자 "
                +" (예시2)힘들지만 조금만 더 힘내자, 너에겐 내가 있잖아";
        String fullMessage3 = prompt3 + everyMessage;

        result[0] = chatClient.call(fullMessage1);
        result[1] =chatClient.call(fullMessage2);
        result[2] =chatClient.call(fullMessage3);


        return result;
    }

    /* 사용자의 감정 추출 */
    @Override
    public String getEmotion(String text) {
        String prompt = "이글은 사용자가 느낀 감정에 대한 글이다. 이를 보고 사용자의 현재 감정을 (기쁨,슬픔, 평온, 분노, 불안) 중 하나의 감정으로 감정을 분류해줘. 대답할때는 기쁨,슬픔 이렇게 두글자 단어로만 답변을 해줘.-> ";
        String fullMessage = prompt + text;
        return chatClient.call(fullMessage);
    }

    /* 추천 유튜브 링크 전송 -2개 랜덤 */
    @Override
    public String[] getRandomLink(String emotion) {

        List<String> youtubeLinks = new ArrayList<>();

        // TODO : 각 감정에 따른 유튜브 링크 실제로 삽입하기

        // 감정에 따른 유튜브 링크 설정
        switch (emotion) {
            case "기쁨" -> youtubeLinks = new ArrayList<>(List.of(
                    "https://youtube.com/link1",
                    "https://youtube.com/link2",
                    "https://youtube.com/link3",
                    "https://youtube.com/link4",
                    "https://youtube.com/link5",
                    "https://youtube.com/link6",
                    "https://youtube.com/link7",
                    "https://youtube.com/link8",
                    "https://youtube.com/link9",
                    "https://youtube.com/link10"
            ));
            case "슬픔" -> youtubeLinks = new ArrayList<>(List.of(
                    "https://youtube.com/link11",
                    "https://youtube.com/link12",
                    "https://youtube.com/link13",
                    "https://youtube.com/link14",
                    "https://youtube.com/link15",
                    "https://youtube.com/link16",
                    "https://youtube.com/link17",
                    "https://youtube.com/link18",
                    "https://youtube.com/link19",
                    "https://youtube.com/link20"
            ));
            case "평온" -> youtubeLinks = new ArrayList<>(List.of(
                    "https://youtube.com/link21",
                    "https://youtube.com/link22",
                    "https://youtube.com/link23",
                    "https://youtube.com/link24",
                    "https://youtube.com/link25",
                    "https://youtube.com/link26",
                    "https://youtube.com/link27",
                    "https://youtube.com/link28",
                    "https://youtube.com/link29",
                    "https://youtube.com/link30"
            ));
            case "분노" -> youtubeLinks = new ArrayList<>(List.of(
                    "https://youtube.com/link31",
                    "https://youtube.com/link32",
                    "https://youtube.com/link33",
                    "https://youtube.com/link34",
                    "https://youtube.com/link35",
                    "https://youtube.com/link36",
                    "https://youtube.com/link37",
                    "https://youtube.com/link38",
                    "https://youtube.com/link39",
                    "https://youtube.com/link40"
            ));
            case "불안" -> youtubeLinks = new ArrayList<>(List.of(
                    "https://youtube.com/link41",
                    "https://youtube.com/link42",
                    "https://youtube.com/link43",
                    "https://youtube.com/link44",
                    "https://youtube.com/link45",
                    "https://youtube.com/link46",
                    "https://youtube.com/link47",
                    "https://youtube.com/link48",
                    "https://youtube.com/link49",
                    "https://youtube.com/link50"
            ));
        }

        Collections.shuffle(youtubeLinks); // 유튜브 링크를 섞고 앞에서부터 2개 반환
        List<String> links =youtubeLinks.subList(0, 2);

        String[] youtube_link = new String[5] ;
        youtube_link[0] = links.get(0);
        youtube_link[1] = links.get(1);

        return youtube_link;
    }

    @Override
    public void saveReport(Member member, String currentDifficulty, String currentEmotion, String aiAdvice,String video_link1,String video_link2 ) {

        // 1. Report 생성 및 저장
        Report report = Report.builder()
                .member(member)
                .currentDifficulty(currentDifficulty)
                .currentEmotion(currentEmotion)
                .aiAdvice(aiAdvice)
                .build();
        reportRepository.save(report);

        // 2. VideoLink 엔티티 생성 및 Report와 연관 관계 설정 후 저장

        VideoLink link = VideoLink.builder()
                .video_link1(video_link1)
                .video_link2(video_link2)
                .report(report)
                .build();
        videoLinkRepository.save(link);


    }


}


