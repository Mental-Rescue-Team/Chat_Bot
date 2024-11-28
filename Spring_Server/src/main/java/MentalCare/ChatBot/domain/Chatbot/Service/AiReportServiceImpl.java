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
                youtubeVideos.put("연예인 축하영상 ", "https://www.youtube.com/watch?v=_JsemaQu-5w");
                youtubeVideos.put("[TJ노래방] 축하곡 - 축하곡 / TJ Karaoke", "https://www.youtube.com/watch?v=b6K50YlMBe4");
                youtubeVideos.put("Pharrell Williams - Happy (Video)", "https://www.youtube.com/watch?v=ZbZSe6N_BXs");
                youtubeVideos.put("Life Vest Inside - Kindness Boomerang - \"One Day\"", "https://www.youtube.com/watch?v=nwAYpLVyeFU");
                youtubeVideos.put("잔나비 - 나의 기쁨 나의 노래", "https://youtu.be/F2eQObB1IDk?si=vp1AWeTFRbZvatuF");
                youtubeVideos.put("Kool & The Gang - Celebration (Lyrics)", "https://www.youtube.com/watch?v=cIg6odS-fA0");
                youtubeVideos.put("Joyful Jams: A Happy Mood Music Mix for Positivity and Energy!\"", "https://www.youtube.com/watch?v=taj1WzeKUSw");
                youtubeVideos.put("Jonas Brothers - Celebrate! (Official Lyric Video)", "https://www.youtube.com/watch?v=KL-AQbhtNn0");
                youtubeVideos.put("HAPPY LUCKY JOYFUL MUSIC", "https://www.youtube.com/watch?v=d1txDGWgMr8");
                youtubeVideos.put("Happy Moments Video Compilation 2016", "https://www.youtube.com/watch?v=8BrCACPmdo4");
                break;
            case "슬픔":
                youtubeVideos.put("감당할 수 없는 슬픔을 마주쳤을 때 - 김창옥 [김창옥tv 몰아보기]", "https://www.youtube.com/watch?v=oGMoT0BlOvs&pp=ygUQ7Iqs7ZSU7J2EIOq3ueuztQ%3D%3D");
                youtubeVideos.put("[김창옥TV 정기강연회 32회] 슬픔 안에서", "https://www.youtube.com/watch?v=4ALlNKg0fUo&pp=ygUQ7Iqs7ZSU7J2EIOq3ueuztQ%3D%3D");
                youtubeVideos.put("This could be why you're depressed or anxious | Johann Hari | TED", "https://www.youtube.com/watch?v=MB5IX-np5fE");
                youtubeVideos.put("How to overcome depression", "https://www.youtube.com/watch?v=Gljq2FHzTvY");
                youtubeVideos.put("Jordan Peterson on Overcoming Depression: Strategies for Healing", "https://www.youtube.com/watch?v=aJBDj-cS3Ls");
                youtubeVideos.put("영원한 이별을 준비하는 사람들에게 : Dean Lewis - How Do I Say Goodbye", "https://www.youtube.com/watch?v=ATi551n86HI");
                youtubeVideos.put("지친 마음을 달래주는 음악 ", "https://www.youtube.com/watch?v=m7GOmncIU5A");
                youtubeVideos.put("Daily Routine To Fight Off Depression", "https://www.youtube.com/watch?v=Y9A5wuTtblw");
                youtubeVideos.put("마음의 슬픔 어떻게 극복할까? / 슬픔에 빠진 사람들을 위한 슬픔 이겨내는 법", "https://www.youtube.com/watch?v=P_CWjaaHDkM&pp=ygUQ7Iqs7ZSU7J2EIOq3ueuztQ%3D%3D");
                youtubeVideos.put("관계로 인한 우울, 미련, 집착, 분노 극복하려면?\n", "https://www.youtube.com/watch?v=bQgMmbfdJEc&pp=ygUQ7Iqs7ZSU7J2EIOq3ueuztQ%3D%3D");
                break;
            case "평온":
                youtubeVideos.put("깊은 안정을 돕는 릴렉스음악 °• 잔잔하고 평온한 시간", "https://www.youtube.com/watch?v=quUl8dlKA8E&pp=ygUa7Y-J7Jio7ZWg65WMIOuTo-uKlCDrhbjrnpg%3D");
                youtubeVideos.put("숲 속 휴식음악,모닥불 소리,스트레스 해소음악,명상음악 - \"With Coffee\"", "https://www.youtube.com/watch?v=ZQWrleq-NhQ&pp=ygUa7Y-J7Jio7ZWg65WMIOuTo-uKlCDrhbjrnpg%3D");
                youtubeVideos.put("1시간 힐링음악, 스트레스해소음악, 마음이 편안해지는 음악, 위로음악", "https://www.youtube.com/watch?v=m7GOmncIU5A&t=57s&pp=ygUa7Y-J7Jio7ZWg65WMIOuTo-uKlCDrhbjrnpg%3D");
                youtubeVideos.put("The Silent Forest - Chillstep Mix", "https://www.youtube.com/watch?v=18NHV4nJLQc");
                youtubeVideos.put("따뜻함에 녹아 내릴 듯한 편안한 팝 플레이리스트", "https://www.youtube.com/watch?v=G51VCzXGsJ4");
                youtubeVideos.put("빗소리,휴식음악,스트레스 해소음악 - Rainy Afternoon", "https://www.youtube.com/watch?v=gKVy9lEi3Zc");
                youtubeVideos.put("묘한 만족감을 주며 마음이 편해지는 기분좋아지는 영상 & ASMR # 27", "https://www.youtube.com/watch?v=RD36uQYN1vM&pp=ygUQ7Y647JWI7ZWcIOyYgeyDgQ%3D%3D");
                youtubeVideos.put("캬 이게 쏙 들어가네ㅎㅎ 속이 다 시원해지는 편안한 장면들", "https://www.youtube.com/watch?v=iRsDNfGrN30&pp=ygUQ7Y647JWI7ZWcIOyYgeyDgQ%3D%3D");
                youtubeVideos.put("편안한 음악과 함께한 스위스 여행 (4K UHD) 아름다운 자연 풍경 | 4K 비디오 Ultra HD", "https://www.youtube.com/watch?v=VGkoaHyVUdg");
                youtubeVideos.put("4k자연영상| 힐링영상 I 아름다운산영상풍경 | 보고만 있어도 행복한 | 떠나고 싶어지는 영상", "https://www.youtube.com/watch?v=w89Hqm1IG8M&pp=ygUQ7Y647JWI7ZWcIOyYgeyDgQ%3D%3D");
                break;
            case "분노":
                youtubeVideos.put("8 Ways to Overcome Anger", "https://www.youtube.com/watch?v=zpucCXdOSH8");
                youtubeVideos.put("5 Powerful Ways to Get Over Feeling Angry (Anger Management Techniques)", "https://www.youtube.com/watch?v=T6eQz7J7xSY");
                youtubeVideos.put("Relentless: Overcoming Anger - with Nick Vujicic", "https://www.youtube.com/watch?v=CPJlLmlyc60");
                youtubeVideos.put("Guided Meditation for Anger and Frustration", "https://www.youtube.com/watch?v=muDQm3S7mEI");
                youtubeVideos.put("Anger Management: Overcoming Destructive Anger", "https://www.youtube.com/watch?v=A7DvbZ8ORbM");
                youtubeVideos.put("HOW TO OVERCOME RESENTMENT & let go of bitterness, anger & grudges / forgiving and moving on PODCAST", "https://www.youtube.com/watch?v=PnjEwU24D2g");
                youtubeVideos.put("Living Better | Overcoming Anger", "https://www.youtube.com/watch?v=86NXF5YzSHg");
                youtubeVideos.put("화를 다스리는 법│아주대학교 김경일 교수", "https://www.youtube.com/watch?v=eDNTuDBqHSY&pp=ygUa67aE64W466W8IOuLpOyKpOumrOuKlCDrspU%3D");
                youtubeVideos.put("분노조절장애? 분노를 다스리는 5가지 방법", "https://www.youtube.com/watch?v=cOdzCMEengo&pp=ygUa67aE64W466W8IOuLpOyKpOumrOuKlCDrspU%3D");
                youtubeVideos.put("[playlist] 개빡칠 때 듣는 노래 | 화났을 때 듣기 좋은 팝송 모음", "https://www.youtube.com/watch?v=71hZutqP_cM&pp=ygUY7ZmU64KgIOuVjCDrk6PripQg64W4656Y");
                break;
            case "불안":
                youtubeVideos.put("How to cope with anxiety | Olivia Remes | TEDxUHasselt", "https://www.youtube.com/watch?v=WWloIAQpMcQ");
                youtubeVideos.put("불안해소 명상, 불안을 극복하는 법, 마음이 불안할때 도움이 되는 명상", "https://www.youtube.com/watch?v=kmdqb6Iv5-4&pp=ygUU67aI7JWIIOq3ueuztSDrqoXsg4E%3D");
                youtubeVideos.put("마음의 안정이 필요하시다면? 함께 명상해요 | 클래스e - 김주환의 마음근력 키우는 내면소통 명상", "https://www.youtube.com/watch?v=AHv3XC9tXcs&pp=ygUU67aI7JWIIOq3ueuztSDrqoXsg4E%3D");
                youtubeVideos.put("명상 가이드 - 불안, 걱정, 두려움 즉시 해소", "https://www.youtube.com/watch?v=at4NzMuQiP4&pp=ygUU67aI7JWIIOq3ueuztSDrqoXsg4E%3D");
                youtubeVideos.put("잠에 들며 공황장애, 불안장애의 치유를 돕는 수면 명상가이드", "https://www.youtube.com/watch?v=zAIZpNbYytI");
                youtubeVideos.put("술과 게임으로도 해결이 되나요? 전두엽을 자극해야 한다! ", "https://www.youtube.com/watch?v=7XCx1XcVP5w&pp=ygUX67aI7JWIIO2VtOyGjO2VmOuKlCDrspU%3D");
                youtubeVideos.put("누구나 불안하지만 분명 누구나 극복합니다. 불안장애 극복방법 알기 | 박서희 정신과의사", "https://www.youtube.com/watch?v=fMygYb9njvI&pp=ygUX67aI7JWIIO2VtOyGjO2VmOuKlCDrspU%3D");
                youtubeVideos.put("불안과 무기력을 다스리는 법 | 김경일 아주대학교 심리학과 교수", "https://www.youtube.com/watch?v=7cfn8-SgVbA&pp=ygUX67aI7JWIIO2VtOyGjO2VmOuKlCDrspU%3D");
                youtubeVideos.put("스트레스해소음악,명상음악,이완음악,스파음악,수면음악 - Warm Candle", "https://www.youtube.com/watch?v=cRbYxsLHyJU&pp=ygUa67aI7JWIIO2VtOyGjO2VmOuKlCDrhbjrnpg%3D");
                youtubeVideos.put("[스트레스 호르몬 40% 감소] 참 편안하다. 듣는것만으로 마음의 평안이 찾아오는 치유명상음악", "https://www.youtube.com/watch?v=TyT02nwr8Ss&pp=ygUa67aI7JWIIO2VtOyGjO2VmOuKlCDrhbjrnpg%3D");
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


