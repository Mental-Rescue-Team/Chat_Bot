package MentalCare.ChatBot.global.Exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /*Member Error*/
    ALREADY_EXIST_MEMBER(00, "이미 존재하는 사용자입니다."),
    NOT_FOUND_MEMBER(00,"존재하지 않는 사용자 입니다."),
    JWT_CANNOT_BE_NULL(00,"JWT는 NULL이 될 수 없습니다.");

    /*Admin Error*/

    /*Chatting Error*/

    /*Diary Error*/

    private final int errorCode;
    private final String message;
}
