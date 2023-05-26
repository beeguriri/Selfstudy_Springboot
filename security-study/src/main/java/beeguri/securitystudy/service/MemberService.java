package beeguri.securitystudy.service;

import beeguri.securitystudy.domain.Member;
import beeguri.securitystudy.dto.MemberLoginDto;
import beeguri.securitystudy.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    public Member login(MemberLoginDto params){
        return memberRepository.findByUserid(params.getUserid())
                .orElseThrow();
    }
}
