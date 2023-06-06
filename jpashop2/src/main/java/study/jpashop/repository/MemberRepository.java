package study.jpashop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.jpashop.domain.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //스프링 데이터 jpa가 메서드 룰에 따라
    //select m from Member m where m.name = ?
    List<Member> findByName(String name);
}
