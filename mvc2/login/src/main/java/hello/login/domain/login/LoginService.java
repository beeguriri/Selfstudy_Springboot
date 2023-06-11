package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {

        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null); //로그인 실패

//        Optional<Member> findMember = memberRepository.findByLoginId(loginId);
//
//        Member member = findMember.get();
//
//        if(member.getPassword().equals(password))
//            return member;
//        else return null;

    }
}
