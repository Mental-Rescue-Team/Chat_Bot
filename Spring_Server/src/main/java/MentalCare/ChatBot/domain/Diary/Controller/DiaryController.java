package MentalCare.ChatBot.domain.Diary.Controller;

import MentalCare.ChatBot.domain.Diary.DTO.Response.DateEmoji;
import MentalCare.ChatBot.domain.Diary.Entity.Diary;
import MentalCare.ChatBot.domain.Diary.Repository.DiaryRepository;
import MentalCare.ChatBot.domain.Diary.Service.DiaryService;
import MentalCare.ChatBot.domain.Diary.Service.DiaryUtil;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Diary", description = "일기 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
@CrossOrigin(origins = "http://localhost:8000")
public class DiaryController {

    private final DiaryService diaryService;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryUtil diaryUtil;

    /**
     * 일기 저장 API
     * @param text 일기 값
     * @param request 사용자 요청
     * @return 일기값,AI 만화 링크
     */
    @Operation(summary = "일기 저장 버튼", description = " 일기 저장 ")
    @PostMapping("")
    public ResponseEntity<LinkedHashMap<String, String>> DiarySave(@RequestBody String text, HttpServletRequest request) {

        String message = diaryUtil.extractMessageFromJson(text);
        Member member = jwtUtil.extractMemberByRequest(request);
        Diary diary = diaryService.DiaryDaveButton(message,member.getGender(),member);

        return ResponseEntity.ok().body(new LinkedHashMap<String, String>() {{
            put("diaryText",diary.getDiaryText());
            put("comicURL", diary.getTemporaryURL());
        }});
    }

    /**
     * 일기 조회 API
     * @param date 오늘의 날짜
     * @param request 요청
     * @return 일기값, 만화 url 반환
     */
    @Operation(summary = " 일기 조회 ", description = " 클라이언트 측에서 넘어오는 날짜 값을 받아 당일날 일기 본문과 4컷 만화를 반환gksek ")
    @GetMapping("")
    public ResponseEntity<Map<String, String>> DiaryShow(@RequestParam("date") String date,HttpServletRequest request){

        LocalDate diaryDate = diaryUtil.dateParser(date);
        Member member = jwtUtil.extractMemberByRequest(request);
        Diary diary = diaryService.getDiaryByDate(diaryDate,member.getMember_no());
        String url = diaryUtil.returnURL(diaryDate,diary);

        Map<String, String> response = Map.of(
                "diaryText", diary.getDiaryText(),
                "comicURL", url
        );// 응답 데이터 생성

        return ResponseEntity.ok().body(response);
    }

    /**
     * 오늘의 날씨 조회 API
     * @param request
     * @return 오늘의 날씨 정보
     */
    @Operation(summary = "메인 페이지 상단에 표시할 오늘의 날씨 전송 ", description = "오늘 날짜를 받아서, 오늘의 날씨 이름을 클라이언트 측으로 전송 - 날씨이름에 맞는 날씨 사진과 메시지는 클라이언트 측에서 준비 ")
    @GetMapping("/today/weather")
    public ResponseEntity<String> todayWeather(HttpServletRequest request){

        Member member = jwtUtil.extractMemberByRequest(request);
        Diary diary = diaryRepository.findByDiaryDateAndMemberNo(LocalDate.now(), member.getMember_no());
        String todayWeather = diary.getWeather();

        return ResponseEntity.ok().body(todayWeather);
    }

    /**
     * 캘린더에 날씨 이모티콘 로드 API
     * @param month
     * @param request
     * @return
     */
    @Operation(summary = " 메인 페이지의 캘린더에 날씨 이모티콘을 띄우는 기능", description = " 서버측에서 클라이언트 측으로 (날짜-날씨 이름) 을 보내줄테니 클라이언트 측에서는 이를 받아, 각 날짜에 맞는 날씨 이모티콘을 삽입")
    @GetMapping("/every/weathers")
    public ResponseEntity<List<DateEmoji>> loadEmojis(@RequestParam("month") int month, HttpServletRequest request){

        Member member = jwtUtil.extractMemberByRequest(request);
        return ResponseEntity.ok(diaryService.getEveryDateEmoji(month,member));
    }

    /**
     * 배경화면 변화 API
     * @param request
     * @return
     */
    @Operation(summary = "사용자가 오늘 작성한 일기의 감정에 따른 채팅 배경화면 매칭 ", description = "이 메서드로 사용자의 일기에서 도출된 감정을 클라이언트 측에 보내니, 클라이언트츨에서 감정에 맞는 채팅 배경화면을 매칭시킬 것, 만약 사용자가 아직 일기를 작성하지 않아 감정이 없는 경우는 [아직 오늘의 감정이 없음]이라는 메시지를 클라이언트에 보냅니다. 그럼 배경화면은 기본배경화면으로 해주면 좋을 것 같습니다")
    @GetMapping("/background")
    public ResponseEntity<String> getMember_emotion(HttpServletRequest request){

        Member member = jwtUtil.extractMemberByRequest(request);
        return ResponseEntity.ok(diaryService.getMemberEmotion(member));
    }

}
