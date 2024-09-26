//package MentalCare.ChatBot.global.auth.Userdetails;
//
//import MentalCare.ChatBot.domain.Member.Entity.Member;
//import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
//import MentalCare.ChatBot.global.auth.Entity.CustomUserDetails;
//import MentalCare.ChatBot.global.auth.Repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final MemberRepository memberRepository;
//
//    @Override
//    public Optional<Member> loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<Member> member= memberRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return member;
//    }
//}
