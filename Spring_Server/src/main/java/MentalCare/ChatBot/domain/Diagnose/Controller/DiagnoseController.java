package MentalCare.ChatBot.domain.Diagnose.Controller;

import MentalCare.ChatBot.domain.Diagnose.Service.DiagnoseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Diagnose", description = "진단 검사 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DiagnoseController {

    private final DiagnoseService diagnoseService;

    // 입력값 : (Integer)score
    // 출력값 : (String)message

    /* 우울증 검사 - 우울증 건강 설문 -9 (PHQ-9) */
    @Operation(summary = " 우울증 검사 ", description = " 서버측으로 점수를 넘겨주면, 결과를 반환")
    @PostMapping("/diagnose/1")
    public String depression_test(@RequestBody Integer score){

        String result = diagnoseService.test1(score);
        System.out.println(result);

        return result;
    }

    /* 걱정&불안 검사 - 한국판 펜스테이트 걱정 질문지 -(PSWQ-CK) */
    @Operation(summary = " 걱정&불안 검사 ", description = "서버측으로 점수를 넘겨주면, 결과를 반환 ")
    @PostMapping("/diagnose/2")
    public String anxiety_test(@RequestBody Integer score){

        String result = diagnoseService.test2(score);
        System.out.println(result);

        return result;
    }

    /* 자존감 검사 - 로젠버그 자아존중감 척도 (RSES) */
    @Operation(summary = " 자존감 검사 ", description = " 서버측으로 점수를 넘겨주면, 결과를 반환")
    @PostMapping("/diagnose/3")
    public String self_esteem_test(@RequestBody Integer score){

        String result = diagnoseService.test3(score);
        System.out.println(result);

        return result;
    }

    /* 수면장애 검사 - 한국판 불면증 심각도 척도(ISI-K) */
    @Operation(summary = " 수면장애 검사" , description = " 서버측으로 점수를 넘겨주면, 결과를 반환")
    @PostMapping("/diagnose/4")
    public String sleep_disorder_test(@RequestBody Integer score){

        String result = diagnoseService.test4(score);
        System.out.println(result);

        return result;
    }
}
