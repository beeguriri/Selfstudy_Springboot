package study.jpashop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.jpashop.domain.Member;
import study.jpashop.repository.MemberRepository;

import java.util.List;

@Service
//@Transactional // 트랜젝션 안에서 데이터 변경이 일어나야 한다
@Transactional(readOnly = true) //조회에는 readOnly true : 성능최적화
@RequiredArgsConstructor //final 필드의 생성자 만들어줌
public class MemberService {

    private final MemberRepository memberRepository;

//    @Autowired //생성자 인젝션, 생성자 하나일때는 생략 가능
//    //memberService 호출할 때 의존관계 있음을 알려줌
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // 회원 가입
    @Transactional //대부분이 조회기능이고 쓰기기능이 하나만 있을때는 메서드 위에 명시
    public Long join(Member member) {

        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);

        return member.getId();
    }

    // 중복 회원 검증
    // 실무에서는 멀티스레드 검증을 위해 name 에 unique 속성 줌
    private void validateDuplicateMember(Member member) {
        // Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if(!findMembers.isEmpty())
            throw new IllegalStateException("이미 존재하는 회원입니다.");
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 한건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
