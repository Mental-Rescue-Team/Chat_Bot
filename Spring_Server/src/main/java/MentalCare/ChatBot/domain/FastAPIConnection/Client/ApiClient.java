package MentalCare.ChatBot.domain.FastAPIConnection.Client;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.BodyInserters;

@Component
public class ApiClient {

    private final WebClient webClient;

    @Autowired
    public ApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }

    public String sendData(String text) {
        return webClient.post()
                .uri("/predict") // FastAPI의 예측 엔드포인트
                .body(BodyInserters.fromValue(new InputData(text))) // 데이터 전송
                .retrieve() // 요청 보내기
                .bodyToMono(String.class) // 응답을 String으로 변환
                .block(); // 블로킹 호출
    }

    // InputData 클래스
    @Getter
    @Setter
    public static class InputData {

        private String text;

        public InputData(String text) {
            this.text = text;
        }

    }
}