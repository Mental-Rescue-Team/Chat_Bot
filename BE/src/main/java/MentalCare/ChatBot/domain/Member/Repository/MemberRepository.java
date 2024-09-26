package MentalCare.ChatBot.domain.Member.Repository;

import MentalCare.ChatBot.domain.Member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //이메일로 해당 사용자가 존재하는지 확인
    boolean existsByEmail(String email);

    //이메일로 사용자 찾기
    Optional<Member> findByEmail(String email);

    //사용자 이름으로 사용자 찾기
    Optional<Member> findByUsername(String username);

    // 모든 회원 정보 조회
    List<Member> findAll();

    //사용자 아이디로 사용자 찾기
    Optional<Member> findById(Long id);

}
