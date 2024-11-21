package MentalCare.ChatBot.domain.Chatbot.Service;

import MentalCare.ChatBot.domain.Chatbot.Entity.Report;
import MentalCare.ChatBot.domain.Chatbot.Entity.VideoLink;
import MentalCare.ChatBot.domain.Chatbot.Repository.ReportRepository;
import MentalCare.ChatBot.domain.Chatbot.Repository.VideoLinkRepository;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;

import java.util.*;

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
                + "단, 답변에는 이모티콘, 이모지, 특수문자, 그림문자 등을 절대로 포함하지 말아야 한다. "
                +"사용자 현재의 어려움은 무엇인가? ";
        String fullMessage1 =prompt1 + everyMessage;

        String prompt2= "다음 모든 메시지들(사용자와 챗봇의 대화 내용)을 보고 아래 질문에 1~2문장으로 답을 해줘라"
                + "단, 답변에는 이모티콘, 이모지, 특수문자, 그림문자 등을 절대로 포함하지 말아야 한다. "
                +"사용자가 오늘 느꼈던 감정은 무엇인가?";
        String fullMessage2= prompt2 + everyMessage;

        String prompt3 ="다음 모든 메시지들(사용자와 챗봇의 대화 내용)을 보고 아래 질문에 1~2문장으로 답을 해줘라 "
                + "단, 답변에는 이모티콘, 이모지, 특수문자, 그림문자 등을 절대로 포함하지 말아야 한다. "
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
    public Map<String, String>[] getRandomLink(String emotion) {

        //List<String> youtubeLinks = new ArrayList<>();
        // 비디오 제목과 링크를 저장할 Map
        Map<String, String> youtubeVideos = new HashMap<>();

        switch (emotion) {
            case "기쁨":
                youtubeVideos.put("Happy Song 1", "https://www.youtube.com/watch?v=_JsemaQu-5w");
                youtubeVideos.put("Happy Song 2", "https://www.youtube.com/watch?v=b6K50YlMBe4");
                youtubeVideos.put("Happy Song 3", "https://www.youtube.com/watch?v=ZbZSe6N_BXs");
                youtubeVideos.put("Happy Song 4", "https://www.youtube.com/watch?v=nwAYpLVyeFU");
                youtubeVideos.put("Happy Song 5", "https://www.youtube.com/watch?v=L5pUd2VuRnI");
                youtubeVideos.put("Happy Song 6", "https://www.youtube.com/watch?v=cIg6odS-fA0");
                youtubeVideos.put("Happy Song 7", "https://www.youtube.com/watch?v=taj1WzeKUSw");
                youtubeVideos.put("Happy Song 8", "https://www.youtube.com/watch?v=KL-AQbhtNn0");
                youtubeVideos.put("Happy Song 9", "https://www.youtube.com/watch?v=d1txDGWgMr8");
                youtubeVideos.put("Happy Song 10", "https://www.youtube.com/watch?v=8BrCACPmdo4");
                break;
            case "슬픔":
                youtubeVideos.put("Sad Song 1", "https://www.youtube.com/watch?v=oGMoT0BlOvs&pp=ygUQ7Iqs7ZSU7J2EIOq3ueuztQ%3D%3D");
                youtubeVideos.put("Sad Song 2", "https://www.youtube.com/watch?v=4ALlNKg0fUo&pp=ygUQ7Iqs7ZSU7J2EIOq3ueuztQ%3D%3D");
                youtubeVideos.put("Sad Song 3", "https://www.youtube.com/watch?v=MB5IX-np5fE");
                youtubeVideos.put("Sad Song 4", "https://www.youtube.com/watch?v=Gljq2FHzTvY");
                youtubeVideos.put("Sad Song 5", "https://www.youtube.com/watch?v=aJBDj-cS3Ls");
                youtubeVideos.put("Sad Song 6", "https://www.youtube.com/watch?v=ATi551n86HI");
                youtubeVideos.put("Sad Song 7", "https://www.youtube.com/watch?v=m7GOmncIU5A");
                youtubeVideos.put("Sad Song 8", "https://www.youtube.com/watch?v=Y9A5wuTtblw");
                youtubeVideos.put("Sad Song 9", "https://www.youtube.com/watch?v=P_CWjaaHDkM&pp=ygUQ7Iqs7ZSU7J2EIOq3ueuztQ%3D%3D");
                youtubeVideos.put("Sad Song 10", "https://www.youtube.com/watch?v=bQgMmbfdJEc&pp=ygUQ7Iqs7ZSU7J2EIOq3ueuztQ%3D%3D");
                break;
            case "평온":
                youtubeVideos.put("Calm Song 1", "https://www.youtube.com/watch?v=quUl8dlKA8E&pp=ygUa7Y-J7Jio7ZWg65WMIOuTo-uKlCDrhbjrnpg%3D");
                youtubeVideos.put("Calm Song 2", "https://www.youtube.com/watch?v=ZQWrleq-NhQ&pp=ygUa7Y-J7Jio7ZWg65WMIOuTo-uKlCDrhbjrnpg%3D");
                youtubeVideos.put("Calm Song 3", "https://www.youtube.com/watch?v=m7GOmncIU5A&t=57s&pp=ygUa7Y-J7Jio7ZWg65WMIOuTo-uKlCDrhbjrnpg%3D");
                youtubeVideos.put("Calm Song 4", "https://www.youtube.com/watch?v=18NHV4nJLQc");
                youtubeVideos.put("Calm Song 5", "https://www.youtube.com/watch?v=G51VCzXGsJ4");
                youtubeVideos.put("Calm Song 6", "https://www.youtube.com/watch?v=gKVy9lEi3Zc");
                youtubeVideos.put("Calm Song 7", "https://www.youtube.com/watch?v=RD36uQYN1vM&pp=ygUQ7Y647JWI7ZWcIOyYgeyDgQ%3D%3D");
                youtubeVideos.put("Calm Song 8", "https://www.youtube.com/watch?v=iRsDNfGrN30&pp=ygUQ7Y647JWI7ZWcIOyYgeyDgQ%3D%3D");
                youtubeVideos.put("Calm Song 9", "https://www.youtube.com/watch?v=VGkoaHyVUdg");
                youtubeVideos.put("Calm Song 10", "https://www.youtube.com/watch?v=w89Hqm1IG8M&pp=ygUQ7Y647JWI7ZWcIOyYgeyDgQ%3D%3D");
                break;
            case "분노":
                youtubeVideos.put("Anger Song 1", "https://www.youtube.com/watch?v=zpucCXdOSH8");
                youtubeVideos.put("Anger Song 2", "https://www.youtube.com/watch?v=T6eQz7J7xSY");
                youtubeVideos.put("Anger Song 3", "https://www.youtube.com/watch?v=CPJlLmlyc60");
                youtubeVideos.put("Anger Song 4", "https://www.youtube.com/watch?v=muDQm3S7mEI");
                youtubeVideos.put("Anger Song 5", "https://www.youtube.com/watch?v=A7DvbZ8ORbM");
                youtubeVideos.put("Anger Song 6", "https://www.youtube.com/watch?v=PnjEwU24D2g");
                youtubeVideos.put("Anger Song 7", "https://www.youtube.com/watch?v=86NXF5YzSHg");
                youtubeVideos.put("Anger Song 8", "https://www.youtube.com/watch?v=eDNTuDBqHSY&pp=ygUa67aE64W466W8IOuLpOyKpOumrOuKlCDrspU%3D");
                youtubeVideos.put("Anger Song 9", "https://www.youtube.com/watch?v=cOdzCMEengo&pp=ygUa67aE64W466W8IOuLpOyKpOumrOuKlCDrspU%3D");
                youtubeVideos.put("Anger Song 10", "https://www.youtube.com/watch?v=71hZutqP_cM&pp=ygUY7ZmU64KgIOuVjCDrk6PripQg64W4656Y");
                break;
            case "불안":
                youtubeVideos.put("Anxiety Song 1", "https://www.youtube.com/watch?v=WWloIAQpMcQ");
                youtubeVideos.put("Anxiety Song 2", "https://www.youtube.com/watch?v=kmdqb6Iv5-4&pp=ygUU67aI7JWIIOq3ueuztSDrqoXsg4E%3D");
                youtubeVideos.put("Anxiety Song 3", "https://www.youtube.com/watch?v=AHv3XC9tXcs&pp=ygUU67aI7JWIIOq3ueuztSDrqoXsg4E%3D");
                youtubeVideos.put("Anxiety Song 4", "https://www.youtube.com/watch?v=at4NzMuQiP4&pp=ygUU67aI7JWIIOq3ueuztSDrqoXsg4E%3D");
                youtubeVideos.put("Anxiety Song 5", "https://www.youtube.com/watch?v=zAIZpNbYytI");
                youtubeVideos.put("Anxiety Song 6", "https://www.youtube.com/watch?v=7XCx1XcVP5w&pp=ygUX67aI7JWIIO2VtOyGjO2VmOuKlCDrspU%3D");
                youtubeVideos.put("Anxiety Song 7", "https://www.youtube.com/watch?v=fMygYb9njvI&pp=ygUX67aI7JWIIO2VtOyGjO2VmOuKlCDrspU%3D");
                youtubeVideos.put("Anxiety Song 8", "https://www.youtube.com/watch?v=7cfn8-SgVbA&pp=ygUX67aI7JWIIO2VtOyGjO2VmOuKlCDrspU%3D");
                youtubeVideos.put("Anxiety Song 9", "https://www.youtube.com/watch?v=cRbYxsLHyJU&pp=ygUa67aI7JWIIO2VtOyGjO2VmOuKlCDrhbjrnpg%3D");
                youtubeVideos.put("Anxiety Song 10", "https://www.youtube.com/watch?v=TyT02nwr8Ss&pp=ygUa67aI7JWIIO2VtOyGjO2VmOuKlCDrhbjrnpg%3D");
                break;
        }

        // 비디오 제목과 링크를 랜덤으로 섞어서 2개 선택
        List<Map.Entry<String, String>> videoList = new ArrayList<>(youtubeVideos.entrySet());
        Collections.shuffle(videoList);
        List<Map.Entry<String, String>> links =videoList.subList(0, 2);

        // 선택된 링크들을 Map으로 반환
        Map<String, String>[] videoLinks = new Map[2];

        for (int i = 0; i < 2; i++) {
            Map<String, String> videoMap = new HashMap<>();
            videoMap.put(links.get(i).getKey(), links.get(i).getValue());
            videoLinks[i] = videoMap;
        }

        return videoLinks;
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


