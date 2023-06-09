package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        //given
        Member member = new Member("memberA");

        //when
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        //then
        //단건조회 검증
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //count 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        //then
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);

        //then
        assertThat(findMember).isEqualTo(m1);

    }

    @Test
    public void testQuery() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findUser("AAA", 10);

        //then
        assertThat(result.get(0)).isEqualTo(m1);

    }

    @Test
    public void findUsernameList() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<String> result = memberRepository.findUsernameList();

        //then
        for (String s : result) {
            System.out.println("s = "+ s);
        }
    }

    @Test
    public void findMemberDto() throws Exception {
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        //when
        List<MemberDto> result = memberRepository.findMemberDto();

        //then
        for (MemberDto memberDto : result) {
            System.out.println("dto = " + memberDto);
        }
    }

    @Test
    public void findByNames() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        //then
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> findList = memberRepository.findListByUsername("AAAaa");
        Member findMember = memberRepository.findMemberByUsername("AAAaa");
        Optional<Member> findOptionalMember = memberRepository.findOptionalByUsername("AAAaa");

        //then
        System.out.println("findList = " + findList); //empty collection
        System.out.println("findMember = " + findMember); //null
        System.out.println("findOptionalMember = " + findOptionalMember); //Optional.empty
    }

    @Test
    public void paging() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));//0page에서 3개 가져오기

        //when
        //반환타입이 page 이므로 jpa 가 count 쿼리를 날림
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        //내보낼때는 반드시 dto 객체로 내보내기
        Page<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0); //현재 페이지의 번호
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue(); //첫번쨰 페이지냐?
        assertThat(page.hasNext()).isTrue(); //다음페이지가 있냐?
    }

    @Test
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        //영속성 콘텍스트 안에서는 member5는 40살, DB는 member5 나이 41살
//        List<Member> result = memberRepository.findByUsername("member5");
//        Member member5 = result.get(0);
//        System.out.println("member5 = " + member5); //40

        //벌크 연산 이후에는 영속성컨텍스트 다 날려버려야됨.
//        em.clear(); //또는 modifying 옵션에서 true

        List<Member> resultClear = memberRepository.findByUsername("member5");
        Member member5Clear = resultClear.get(0);
        System.out.println("member5Clear = " + member5Clear); //41

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //DB 반영 후 영속성컨텍스트 날리기
        em.flush();
        em.clear();

        //when
        //select Member 쿼리 1번
//        List<Member> members = memberRepository.findAll();
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.Team.class = " + member.getTeam().getClass());
            //select Team 쿼리 2번
            System.out.println("member.Team = " + member.getTeam().getName());
        }

        //쿼리 한번만 나감
        //        List<Member> members = memberRepository.findMemberFetchJoin();

        //then
    }
    
    @Test
    public void queryHint() throws Exception {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();
        
        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");
        em.flush(); //hint : readOnly true : 변경감지(dirty checking) -> update query 안함

        //then
    }
    
    @Test
    public void callCustom() throws Exception {
        //given
        List<Member> result = memberRepository.findMemberCustom();
        //when
        
        //then
    }

    @Test
    public void queryByExample() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        em.persist(new Member("m1", 0, teamA));
        em.persist(new Member("m2", 0, teamA));
        em.flush();
        em.clear();

        //when
        //Probe
        //도메인 객체로 example 을 만듦
        //한계 : inner join 만 가능, 매칭 조건이 너무 단순함 (==만 사용)
        Member member = new Member("m1");
        Team team = new Team("teamA");
        member.setTeam(team);

        ExampleMatcher matcher = ExampleMatcher.matching()
                                                .withIgnorePaths("age");

        Example<Member> example = Example.of(member, matcher);
        List<Member> result = memberRepository.findAll(example);

        //then
        assertThat(result.get(0).getUsername()).isEqualTo("m1");
    }

    @Test
    public void projections() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        em.persist(new Member("m1", 0, teamA));
        em.persist(new Member("m2", 0, teamA));
        em.flush();
        em.clear();

        //when

        //Interface projection
        //UsernameOnly 인터페이스 메서드 만들어주면 구현체는 spring jpa 가 해줌
//        List<UsernameOnlyDto> result = memberRepository.findProjectionsByUsername("m1");

        //Class projection
//        List<UsernameOnlyDto> result = memberRepository.findProjectionsByUsername("m1", UsernameOnlyDto.class);

        List<NestedClosedProjections> result = memberRepository.findProjectionsByUsername("m1", NestedClosedProjections.class);

        for (NestedClosedProjections usernameOnly : result) {
            System.out.println("usernameOnly = " + usernameOnly.getUsername());
            System.out.println("usernameOnlyClass = " + usernameOnly); //프록시객체가 아닌 실제 객체 가져옴
        }

        //then
    }
}