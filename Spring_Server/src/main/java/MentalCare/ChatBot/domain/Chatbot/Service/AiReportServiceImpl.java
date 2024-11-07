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
                + " 챗봇이 사용자에게 마지막으로 조언해주고 싶은 한마디는? ";
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
                    "https://www.youtube.com/watch?v=_JsemaQu-5w",
                    "https://www.youtube.com/watch?v=b6K50YlMBe4",
                    "https://www.youtube.com/watch?v=ZbZSe6N_BXs",
                    "https://www.youtube.com/watch?v=nwAYpLVyeFU",
                    "https://www.youtube.com/watch?v=L5pUd2VuRnI",
                    "https://www.youtube.com/watch?v=cIg6odS-fA0",
                    "https://www.youtube.com/watch?v=taj1WzeKUSw",
                    "https://www.youtube.com/watch?v=KL-AQbhtNn0",
                    "https://www.youtube.com/watch?v=d1txDGWgMr8",
                    "https://www.youtube.com/watch?v=8BrCACPmdo4"
            ));
            case "슬픔" -> youtubeLinks = new ArrayList<>(List.of(
                    "https://www.youtube.com/watch?v=oGMoT0BlOvs&pp=ygUQ7Iqs7ZSU7J2EIOq3ueuztQ%3D%3D",
                    "https://www.youtube.com/watch?v=4ALlNKg0fUo&pp=ygUQ7Iqs7ZSU7J2EIOq3ueuztQ%3D%3D",
                    "https://www.youtube.com/watch?v=MB5IX-np5fE",
                    "https://www.youtube.com/watch?v=Gljq2FHzTvY",
                    "https://www.youtube.com/watch?v=aJBDj-cS3Ls",
                    "https://www.youtube.com/watch?v=ATi551n86HI",
                    "https://www.youtube.com/watch?v=m7GOmncIU5A",
                    "https://www.youtube.com/watch?v=Y9A5wuTtblw",
                    "https://www.youtube.com/watch?v=P_CWjaaHDkM&pp=ygUQ7Iqs7ZSU7J2EIOq3ueuztQ%3D%3D",
                    "https://www.youtube.com/watch?v=bQgMmbfdJEc&pp=ygUQ7Iqs7ZSU7J2EIOq3ueuztQ%3D%3D"
            ));
            case "평온" -> youtubeLinks = new ArrayList<>(List.of(
                    "https://www.youtube.com/watch?v=quUl8dlKA8E&pp=ygUa7Y-J7Jio7ZWg65WMIOuTo-uKlCDrhbjrnpg%3D",
                    "https://www.youtube.com/watch?v=ZQWrleq-NhQ&pp=ygUa7Y-J7Jio7ZWg65WMIOuTo-uKlCDrhbjrnpg%3D",
                    "https://www.youtube.com/watch?v=m7GOmncIU5A&t=57s&pp=ygUa7Y-J7Jio7ZWg65WMIOuTo-uKlCDrhbjrnpg%3D",
                    "https://www.youtube.com/watch?v=18NHV4nJLQc",
                    "https://www.youtube.com/watch?v=G51VCzXGsJ4",
                    "https://www.youtube.com/watch?v=gKVy9lEi3Zc",
                    "https://www.youtube.com/watch?v=RD36uQYN1vM&pp=ygUQ7Y647JWI7ZWcIOyYgeyDgQ%3D%3D",
                    "https://www.youtube.com/watch?v=iRsDNfGrN30&pp=ygUQ7Y647JWI7ZWcIOyYgeyDgQ%3D%3D",
                    "https://www.youtube.com/watch?v=VGkoaHyVUdg",
                    "https://www.youtube.com/watch?v=w89Hqm1IG8M&pp=ygUQ7Y647JWI7ZWcIOyYgeyDgQ%3D%3D"
            ));
            case "분노" -> youtubeLinks = new ArrayList<>(List.of(
                   "https://www.youtube.com/watch?v=zpucCXdOSH8",
                    "https://www.youtube.com/watch?v=T6eQz7J7xSY",
                    "https://www.youtube.com/watch?v=CPJlLmlyc60",
                    "https://www.youtube.com/watch?v=muDQm3S7mEI",
                    "https://www.youtube.com/watch?v=A7DvbZ8ORbM",
                    "https://www.youtube.com/watch?v=PnjEwU24D2g",
                    "https://www.youtube.com/watch?v=86NXF5YzSHg",
                    "https://www.youtube.com/watch?v=eDNTuDBqHSY&pp=ygUa67aE64W466W8IOuLpOyKpOumrOuKlCDrspU%3D",
                    "https://www.youtube.com/watch?v=cOdzCMEengo&pp=ygUa67aE64W466W8IOuLpOyKpOumrOuKlCDrspU%3D",
                    "https://www.youtube.com/watch?v=71hZutqP_cM&pp=ygUY7ZmU64KgIOuVjCDrk6PripQg64W4656Y"
            ));
            case "불안" -> youtubeLinks = new ArrayList<>(List.of(
                   "https://www.youtube.com/watch?v=WWloIAQpMcQ",
                    "https://www.youtube.com/watch?v=kmdqb6Iv5-4&pp=ygUU67aI7JWIIOq3ueuztSDrqoXsg4E%3D",
                    "https://www.youtube.com/watch?v=AHv3XC9tXcs&pp=ygUU67aI7JWIIOq3ueuztSDrqoXsg4E%3D",
                    "https://www.youtube.com/watch?v=at4NzMuQiP4&pp=ygUU67aI7JWIIOq3ueuztSDrqoXsg4E%3D",
                    "https://www.youtube.com/watch?v=zAIZpNbYytI",
                    "https://www.youtube.com/watch?v=7XCx1XcVP5w&pp=ygUX67aI7JWIIO2VtOyGjO2VmOuKlCDrspU%3D",
                    "https://www.youtube.com/watch?v=fMygYb9njvI&pp=ygUX67aI7JWIIO2VtOyGjO2VmOuKlCDrspU%3D",
                    "https://www.youtube.com/watch?v=7cfn8-SgVbA&pp=ygUX67aI7JWIIO2VtOyGjO2VmOuKlCDrspU%3D",
                    "https://www.youtube.com/watch?v=cRbYxsLHyJU&pp=ygUa67aI7JWIIO2VtOyGjO2VmOuKlCDrhbjrnpg%3D",
                    "https://www.youtube.com/watch?v=TyT02nwr8Ss&pp=ygUa67aI7JWIIO2VtOyGjO2VmOuKlCDrhbjrnpg%3D"
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


