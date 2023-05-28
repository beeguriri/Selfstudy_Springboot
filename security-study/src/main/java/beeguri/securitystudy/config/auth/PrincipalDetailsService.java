package beeguri.securitystudy.config.auth;

import beeguri.securitystudy.domain.Member;
import beeguri.securitystudy.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// "/login" 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어있는 "loadUserByUsername" 메서드 실행
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    // 시큐리티 session(내부 Authentication(내부 UserDetails))
    @Override
    //폼에서 username 이라고 지정 (id 입력란)
    public UserDetails loadUserByUsername(String username) {
        Member memberEntity = memberRepository.findByUserid(username)
                .orElseThrow(() -> new NoSuchElementException("Member Not Found"));
        if(memberEntity!=null)
            return new PrincipalDetails(memberEntity);

        return null;
    }
}
