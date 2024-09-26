//package MentalCare.ChatBot.global.auth.Repository;
//
//
//import MentalCare.ChatBot.global.auth.Entity.CustomUserDetails;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Optional;
//
///*
//JpaRepository: Spring Data JPA에서 제공하는 인터페이스로,
//기본적인 CRUD 메서드를 자동으로 구현해줍니다.
//여기서는 User 엔티티와 Long 타입의 ID를 사용하는 리포지토리입니다.
//*/
//public interface UserRepository extends JpaRepository<CustomUserDetails,Long> {
//
//    // 사용자 이름으로 사용자 검색
//    Optional<CustomUserDetails> findByUsername(String username);
//}
