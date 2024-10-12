package MentalCare.ChatBot.domain.FastAPIConnection.Controller;

import MentalCare.ChatBot.domain.FastAPIConnection.Service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    // 작동 순서 : apiController -> apiService -> apiClient -> fast-api
    private final ApiService apiService; // final 키워드로 필드를 선언

    @PostMapping("/call-fastapi")
    public ResponseEntity<String> callFastApi(@RequestBody String text) {
        String response = apiService.callFastApi(text); // FastAPI에 POST 요청
        return ResponseEntity.ok(response);
    }
}