package MentalCare.ChatBot.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>(); //redis에 데이터를 추가, 추출하는 클래스
        template.setConnectionFactory(connectionFactory); //redis와의 연결을 관리
        template.setKeySerializer(new StringRedisSerializer());  //모든 키를 string으로 직렬화한다. redis에 저장될 떄 모두 문자열로 저장
        template.setValueSerializer(new StringRedisSerializer()); //값을 string으로 저장하려고 할때, 직렬화해서 저장
        return template;
    }
}