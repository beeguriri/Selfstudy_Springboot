package study.jpashop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.jpashop.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository //스프링부트의 컴포넌트 스캔 대상 => 스프링빈에 등록
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext //jpa 제공 표준 어노테이션
//    private EntityManager em; // 스프링이 EntityManager 만들어서 주입
    //생성자 인젝션 (em은 원래 @autowired 어노테이션 못쓰고, persistenceContext 써야되는데...)
    private final EntityManager em;

    public void save (Member member) {
        //영속성 컨텍스트에 멤버 객체 넣고
        //트랜젝션 커밋 시점에 DB에 데이터 날라감
        em.persist(member);
    }

    //단건 조회 (type, pk)
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    //여러건 조회(JPQL)
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //여러건 조회(JPQL) 파라미터 바인딩
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name= :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
