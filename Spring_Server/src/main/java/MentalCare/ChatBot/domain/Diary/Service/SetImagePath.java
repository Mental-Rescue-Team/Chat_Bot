package MentalCare.ChatBot.domain.Diary.Service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class SetImagePath {

    /* 저장할 이미지url과 이미지를 저장할 내 서버의 디렉토리 경로를 받아서 디렉토리경로를 반환한다. */
    public String saveImagePath(String imageUrl, String saveDir) throws IOException {

        return downloadImage(imageUrl, saveDir);
    }

    /* 이미지 임시 url(2시간만 조회 가능한 url)을 받아와 내 서버의 특정 디렉토리에 저장 */
    public static String downloadImage(String imageUrl, String saveDir) throws IOException {
        // 이미지를 다운로드할 URL 객체 생성
        URL url = new URL(imageUrl);

        // 파일 이름을 UUID로 생성하여 고유하게 설정 (파일명 충돌 방지)
        String fileName = UUID.randomUUID().toString() + ".png"; // 확장자는 URL에서 확인해야 함
        Path targetPath = Paths.get(saveDir, fileName); // 저장할 파일 경로

        // 이미지 다운로드
        try (InputStream in = url.openStream()) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING); // 파일 저장
        }
        String urlPath="http://localhost:8080/images/";

        return urlPath + fileName;  // 저장한 파일 경로가 아닌 사용자가 인식 할 수 있는 경로 반환
    }
}
