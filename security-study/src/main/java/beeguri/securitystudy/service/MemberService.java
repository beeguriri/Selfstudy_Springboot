package beeguri.securitystudy.service;

import beeguri.securitystudy.domain.Member;
import beeguri.securitystudy.dto.MemberJoinDto;
import beeguri.securitystudy.dto.MemberLoginDto;
import beeguri.securitystudy.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        return memberRepository.findByUserid(params.getUserid())
                .orElseThrow();
    }
}
