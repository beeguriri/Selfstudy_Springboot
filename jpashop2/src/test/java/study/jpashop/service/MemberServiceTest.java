package study.jpashop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.jpashop.domain.Member;
import study.jpashop.repository.MemberRepositoryOld;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//EntityManager를 통한 모든 데이터 변경은 transaction 안에서 이루어져야함
//test에 transactional 어노테이션이 있으면 테스트 실행 후 롤백을 함
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepositoryOld memberRepositoryOld;
    @Autowired EntityManager em;

    //em에서 persist를 할때는 쿼리 insert 문이 안나감
    // transaction commit 시점에 영속성 객체가 insert문으로 나감
    @Test
//    @Rollback(false) //DB에 저장되는거 보기위해
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        em.flush();
        assertEquals(member, memberRepositoryOld.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
    }

}