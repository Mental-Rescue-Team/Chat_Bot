package MentalCare.ChatBot.domain.Member.Repository;

import MentalCare.ChatBot.domain.Member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //이메일로 해당 사용자가 존재하는지 확인
    boolean existsByEmail(String email);

    //이메일로 사용자 찾기
    Optional<Member> findByEmail(String email);

    //사용자 이름으로 사용자 찾기
    Optional<Member> findByUsername(String username);

    // 사용자 이름만 리스트로 반환하는 메서드
    @Query("SELECT m.username FROM Member m")
    List<String> findAllUsernames();

    //사용자 아이디로 사용자 찾기
    Optional<Member> findById(Long id);

    // username으로 member_no를 찾는 메서드 추가
    //@Query("SELECT m.member_no FROM Member m WHERE m.username = :username")
    //Optional<Long> findMemberNoByUsername(@Param("username") String username);


}
