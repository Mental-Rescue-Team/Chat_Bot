package MentalCare.ChatBot.domain.Diagnose.Controller;

import MentalCare.ChatBot.domain.Diagnose.Service.DiagnoseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Diagnose", description = "진단 검사 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/diagnose")
public class DiagnoseController {

    private final DiagnoseService diagnoseService;

    /**
     * 우울증 검사 - 우울증 건강 설문 -9 (PHQ-9)
     * @param score
     * @return
     */
    @Operation(summary = " 우울증 검사 ", description = " 서버측으로 점수를 넘겨주면, 결과를 반환 - 다만 클라이언트 측에서는 다른 API와는 다르게 Json 객체를 넘겨 주는 것이 아니라 , 3,10,15 처럼 숫자만 넘겨주셔야 오류가 안납니다.")
    @PostMapping("/1")
    public String depression_test(@RequestBody Integer score){

        String result = diagnoseService.test1(score);
        log.info(" result : {}",result);

        return result;
    }

    /**
     * 걱정&불안 검사 - 한국판 펜스테이트 걱정 질문지 -(PSWQ-CK)
     * @param score
     * @return
     */
    @Operation(summary = " 걱정&불안 검사 ", description = "서버측으로 점수를 넘겨주면, 결과를 반환 - 다만 클라이언트 측에서는 다른 API와는 다르게 Json 객체를 넘겨 주는 것이 아니라 , 3,10,15 처럼 숫자만 넘겨주셔야 오류가 안납니다. ")
    @PostMapping("/2")
    public String anxiety_test(@RequestBody Integer score){

        String result = diagnoseService.test2(score);
        log.info(" result : {}",result);

        return result;
    }

    /**
     * 자존감 검사 - 로젠버그 자아존중감 척도 (RSES)
     * @param score
     * @return
     */
    @Operation(summary = " 자존감 검사 ", description = " 서버측으로 점수를 넘겨주면, 결과를 반환 - 다만 클라이언트 측에서는 다른 API와는 다르게 Json 객체를 넘겨 주는 것이 아니라 , 3,10,15 처럼 숫자만 넘겨주셔야 오류가 안납니다.")
    @PostMapping("/3")
    public String self_esteem_test(@RequestBody Integer score){

        String result = diagnoseService.test3(score);
        log.info(" result : {}",result);

        return result;
    }

    /**
     * 수면장애 검사 - 한국판 불면증 심각도 척도(ISI-K)
     * @param score
     * @return
     */
    @Operation(summary = " 수면장애 검사" , description = " 서버측으로 점수를 넘겨주면, 결과를 반환 - 다만 클라이언트 측에서는 다른 API와는 다르게 Json 객체를 넘겨 주는 것이 아니라 , 3,10,15 처럼 숫자만 넘겨주셔야 오류가 안납니다.")
    @PostMapping("/4")
    public String sleep_disorder_test(@RequestBody Integer score){

        String result = diagnoseService.test4(score);
        log.info(" result : {}",result);

        return result;
    }
}
