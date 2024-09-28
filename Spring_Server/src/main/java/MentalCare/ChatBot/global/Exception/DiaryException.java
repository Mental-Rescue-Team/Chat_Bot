package MentalCare.ChatBot.global.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DiaryException extends RuntimeException{

    private final ErrorCode errorcode;

}
