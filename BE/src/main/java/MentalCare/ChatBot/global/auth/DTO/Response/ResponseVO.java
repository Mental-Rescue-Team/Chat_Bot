package MentalCare.ChatBot.global.auth.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseVO<T> {

    private T data; // 응답 데이터
    private String message; // 응답 메시지 (선택적)

    // 메시지 필드가 없는 경우를 처리하는 생성자
    public ResponseVO(T data) {
        this.data = data;
        this.message = null;
    }

}
