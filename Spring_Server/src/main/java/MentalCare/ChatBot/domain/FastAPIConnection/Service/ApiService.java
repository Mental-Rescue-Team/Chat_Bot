package MentalCare.ChatBot.domain.FastAPIConnection.Service;

import MentalCare.ChatBot.domain.FastAPIConnection.Client.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final ApiClient apiClient;

    public String callFastApi(String text) {
        return apiClient.sendData(text); // FastAPI에 데이터 전송
    }
}
