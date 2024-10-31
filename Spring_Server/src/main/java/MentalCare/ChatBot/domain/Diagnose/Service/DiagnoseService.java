package MentalCare.ChatBot.domain.Diagnose.Service;

public interface DiagnoseService {

    /* 우울증 검사 서비스 */
    String test1(Integer score);

    /* 걱정&불안 검사 */
    String test2(Integer score);

    /* 자존감 검사 */
    String test3(Integer score);

    /* 수면장애 검사 */
    String test4(Integer score);
}
