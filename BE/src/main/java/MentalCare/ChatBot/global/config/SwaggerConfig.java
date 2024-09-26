package MentalCare.ChatBot.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .version("v1.0.0")                // 버전 기록
                        .title("Mental Rescue Team API")                // API 명세서 제목
                        .description("멘탈 구조대 API 명세서"));   // 상세

    }
}