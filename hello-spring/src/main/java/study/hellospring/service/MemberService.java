package study.hellospring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.hellospring.domain.Member;
import study.hellospring.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {
    
    private final MemberRepository memberRepository;

    //dependency injection
    public MemberService(MemberRepository memberRepository) {

        this.memberRepository = memberRepository;
    }
    
    // 회원가입
    public Long join(Member member){

        long start = System.currentTimeMillis();

        try {
            validateDuplicateMember(member); //중복회원 검증
            memberRepository.save(member);
            return member.getId();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
//            System.out.println("join = " + timeMs + "ms");
        }

    }

    // optional 일때 쓸 수 있는 ifPresent
    // method 뽑을 때 쓰는 단축키 : ctrl + alt + m
    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
            .ifPresent(m -> {
                throw new IllegalStateException("이미 존재하는 회원입니다.");
            });
    }

    // 전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
