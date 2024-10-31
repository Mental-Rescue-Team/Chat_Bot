package MentalCare.ChatBot.domain.Diagnose.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiagnoseServiceImpl implements DiagnoseService {


    // 입력값 : (Integer)score
    // 출력값 : (String)message

    /* 우울증 검사 - 결과는 5단계 */
    // Section : A,B,C,D,E
    @Override
    public String test1(Integer score) {

        String section=" ";

        if (0 <= score && score <= 4) section ="A";
        else if (5 <= score && score <= 9) section="B";
        else if (10 <= score && score <= 14) section="C";
        else if (15 <= score && score <= 19) section="D";
        else if (20 <= score && score <= 27) section="E";
        else section = "Unknown";

        return switch (section) {
            case "A" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 최소한의 우울감 \" 입니다.\n"
                    + "대부분의 사람들에게 흔히 나타나는 경미한 우울감으로, 특별한 치료가 필요하지 않은 경우가 많습니다.";
            case "B" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 경미한 우울증 \" 입니다.\n"
                    + "경미한 우울감을 나타내며, 상담을 통해 상태를 확인하고 증상이 지속되거나 악화될 경우 추가적인 치료를 고려할 수 있습니다.";
            case "C" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 중간 정도의 우울증 \" 입니다.\n"
                    + "치료가 필요한 수준의 우울증으로 간주됩니다. 치료 옵션으로는 상담 치료, 생활 습관 개선, 필요시 약물 치료 등을 고려할 수 있습니다.";
            case "D" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 중증 우울증 가능성 \" 입니다.\n"
                    + "중등도 우울증 이상으로, 전문적인 개입이 필요할 수 있습니다. 상담 치료나 약물 치료를 포함한 다각적인 접근이 추천됩니다.";
            case "E" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 심한 우울증 \" 입니다.\n"
                    + "매우 심각한 우울증으로 간주되며, 긴급한 치료가 필요합니다. 약물 치료와 심리 치료를 조합한 치료 계획이 권장됩니다.";
            default -> "검사 결과가 정상 범주에 부합하지 않습니다. 다시한번 테스트를 시도해보세요!";
        };
    }

    /* 걱정&불안 검사 - 결과는 4단계 */
    // Section : F,G,H,I
    @Override
    public String test2(Integer score) {

        String section=" ";

        if (0 <= score && score <= 50) section ="F";
        else if (51 <= score && score <= 79) section="G";
        else if (80 <= score && score <= 89) section="H";
        else if (90 <= score && score <= 100) section="I";
        else section = "Unknown";

        return switch (section) {
            case "F" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 정상 \" 입니다.\n"
                    + " 걱정과 불안이 적고, 일상생활에 큰 영향을 미치지 않는 상태입니다. 감정을 잘 관리하고 있으며, 스트레스에 대처하는 능력이 있습니다.긍정적인 자기 대화와 감사 일기를 통해 긍정적인 감정을 유지하세요.";
            case "G" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 경미한 걱정&불안증 \" 입니다.\n"
                    + " 가끔씩 걱정과 불안이 느껴지지만, 일상생활에 큰 지장을 주지 않는 정도입니다. 때때로 걱정이 쌓일 수 있으나, 대처 능력이 있습니다.심호흡, 명상, 요가 등 이완 기법을 통해 스트레스를 줄이세요";
            case "H" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 중한 걱정&불안증 \" 입니다.\n"
                    + " 걱정과 불안이 자주 느껴지며, 때때로 일상생활에 지장을 줄 수 있는 수준입니다. 문제를 해결하기 위해 많은 에너지를 소모하게 됩니다.규칙적인 운동과 자기 관리로 신체적, 정서적 건강을 유지하세요.";
            case "I" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 심한 걱정&불안증 \" 입니다.\n"
                    + " 강한 걱정과 불안으로, 일상생활에 심각한 영향을 미치는 상태입니다. 걱정이 매우 강하게 느껴지며, 종종 통제하기 어려운 상황입니다.즉각적인 전문가의 도움을 받는 것이 중요합니다. 심리치료나 상담을 통해 적절한 지원을 받아보세요.";
            default -> "검사 결과가 정상 범주에 부합하지 않습니다. 다시한번 테스트를 시도해보세요!";
        };
    }

    /* 자존감 검사 - 결과는 5단계 */
    // Section : J,K,L,M,N
    @Override
    public String test3(Integer score) {

        String section=" ";

        if (1 <= score && score <= 18) section ="J";
        else if (19 <= score && score <= 24) section="K";
        else if (25 <= score && score <= 38) section="L";
        else if (39 <= score && score <= 44) section="M";
        else if (45 <= score) section="N";
        else section = "Unknown";

        return switch (section) {
            case "J" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 매우 낮은 자존감 \" 입니다.\n"
                    + "스스로를 무가치하고 볼품없는 사람으로 여기며 자존감이 매우 낮은 수준으로 나타납니다. 이러한 양상은 우울이나 기타 극심한 스트레스 사건과 같은 정서적 상황적 요인들과 밀접한 관련이 있습니다.\n"
                    + "이에 대한 탐색이 필히 병행되어야 할 것으로 보입니다. ";
            case "K" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 낮은 자존감 \" 입니다.\n"
                    + "스스로를 무가치하고 볼품없다고 여기며 자존감이 다소 낮은 수준으로 나타납니다. 대개 낮은 자존감이 우울이나 스트레스 사건같은 정서적 상황적 요인들과 관련이 높아,\n"
                    + "이에 대한 탐색이 당신을 이해하는데 도움을 줄 수 있을 것으로 판단됩니다.";
            case "L" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 보통의 자존감 \" 입니다.\n"
                    + "보통 수준의 자존감이 나타납니다.\n"
                    + "실수나 실패에 대해 스스로를 비판하기보다는, 자신에게 친절하고 이해심을 가지세요. 모든 사람은 실수를 합니다."
                    + "자신을 사랑하는 시간을 가져보세요!";
            case "M" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 높은 자존감 \" 입니다.\n"
                    + "자신을 수용하고 존중하며 자존감이 다소 높은 수준으로 나타납니다."
                    + "더 나은 세상을 위해 당신의 재능을 활용하세요! "
                    + "당신의 재능과 자원을 사용하여 주변에 긍정적인 변화를 만들어갈 수 있는 힘이 있습니다.";
            case "N" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 매우 높은 자존감 \" 입니다.\n"
                    + "자신을 수용하고 존중하며, 스스로를 가치 있다고 여기는 능력이 있습니다.\n"
                    + "자신의 경험과 지혜를 다른 사람과 나누며, 멘토 역할을 해보세요. 이를 통해 자신뿐만 아니라 다른 사람의 성장에도 기여할 수 있습니다.";
            default -> "검사 결과가 정상 범주에 부합하지 않습니다. 다시한번 테스트를 시도해보세요!";
        };
    }

    /* 수면장애 검사 - 결과는 4단계 */
    // Section :O,P,Q,R
    @Override
    public String test4(Integer score) {

        String section=" ";

        if (0 <= score && score <= 7) section ="O";
        else if (8 <= score && score <= 14) section="P";
        else if (15 <= score && score <= 21) section="Q";
        else if (22 <= score && score <= 28) section="R";
        else section = "Unknown";

        return switch (section) {
            case "O" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 불면증 아님 \" 입니다.\n"
                    + "수면 문제가 없으며, 충분한 수면을 취하고 있는 상태입니다. 일상 생활에 지장이 없습니다.\n"
                    + "건강한 수면 습관을 계속해서 지키고, 스트레스를 관리하는 방법을 탐색해 보세요.";
            case "P" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 경미한 수준의 불면증 \" 입니다.\n"
                    + "가끔 수면에 어려움을 겪지만, 일상 생활에는 큰 영향을 미치지 않는 상태입니다.\n "
                    + "스트레스 관리 및 이완 기법을 시도해 보세요.";
            case "Q" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 중한 수준의 불면증 \" 입니다.\n"
                    + "수면에 대한 문제가 상당히 증가하고 있으며, 일상 생활에서의 기능에 지장을 주는 상태입니다. \n"
                    + "수면 환경을 적극적으로 개선해보세요.\n"
                    + "수면 문제가 지속된다면, 전문가의 도움을 받는 것이 좋습니다.";
            case "R" -> "당신의 점수는 " + score + "점입니다.\n"
                    + "진단 결과는 \" 심각한 수준의 불면증 \" 입니다.\n"
                    + "수면 문제가 매우 심각하며, 일상 생활에 큰 영향을 미치는 상태입니다. 지속적인 피로와 집중력 저하가 나타날 수 있습니다.\n "
                    + "자신의 수면 패턴과 생활 방식을 면밀히 분석해야 합니다.\n "
                    + "즉시 전문가의 진료를 받는 것이 중요합니다. 심리적 또는 신체적 원인이 있을 수 있습니다.";
            default -> "검사 결과가 정상 범주에 부합하지 않습니다. 다시한번 테스트를 시도해보세요!";

        };
    }
}
