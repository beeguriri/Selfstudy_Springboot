package beeguri.securitystudy.service;

import beeguri.securitystudy.domain.Member;
import beeguri.securitystudy.dto.MemberJoinDto;
import beeguri.securitystudy.dto.MemberLoginDto;
import beeguri.securitystudy.repository.MemberRepository;
import com.sun.xml.bind.v2.runtime.output.Encoded;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public void createMember(MemberJoinDto params) {
        String rawPassword = params.getPassword();
        String encodingPassword = bCryptPasswordEncoder.encode(rawPassword);
        memberRepository.save(new Member(params.getUserid(), encodingPassword, "ROLE_USER"));
    }

    public Member login(MemberLoginDto params){
        Optional<Member> findMember = memberRepository.findByUserid(params.getUserid());
        if(findMember.isPresent()) {
            if (bCryptPasswordEncoder.matches(params.getPassword(), findMember.get().getPassword()))
                return findMember.get();
            else return new Member(params.getUserid(), null, null);
        } else
            return new Member(null, null, null);
    }
}
