package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryException;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){

        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

    }

    @Test
    public void startJPQL() {
        //member1 찾기
        String qlString = "select m from Member m where m.username = :username";
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() {

//        QMember m = new QMember("m"); //따로 alias 지정

        //컴파일 타임에 오류 잡기
        //static import
        Member findMember = queryFactory
                            .select(member)
                            .from(member)
                            .where(member.username.eq("member1"))
                            .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void search() {

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
        assertThat(findMember.getAge()).isEqualTo(10);

    }

    @Test
    public void searchAndParam() {

        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10)
                )
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
        assertThat(findMember.getAge()).isEqualTo(10);

    }

    @Test
    public void resultFetch() {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        //결과가 두개 이상일 경우 NonUniqueResultException
//        Member fetchOne = queryFactory
//                .selectFrom(member)
//                .fetchOne();

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();

        // fetchResults() , fetchCount() Deprecated
        Long count = queryFactory
                .select(member.count())
                .from(member)
                .fetchOne();

        System.out.println("count = " + count);
    }

    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();

    }

    @Test
    public void paging1() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1) //0부터 시작
                .limit(2)
                .fetch();

        for (Member member : result) {
            System.out.println("member = " + member.getUsername());
//            member = member3
//            member = member2
        }
        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    public void aggregation() {
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);

    }

    //팀의 이름과 각 팀의 평균 연령을 구하라!
    @Test
    public void group() throws Exception {

        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);

    }

    //팀A에 소속된 모든 회원
    @Test
    public void join() {
        List<Member> result = queryFactory
                .selectFrom(member)
//                .join(member.team, team)
                .leftJoin(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }

    //회원의 이름이 팀 이름과 같은 회원 조회
    @Test
    public void theta_join() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        for (Member member : result) {
            System.out.println("member = " + member);
        }

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    //회원과 팀을 조인하면서,
    //팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
    @Test
    public void join_on_filtering() {

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team) //left 가 member 이므로 member 정보는 다 가져옴
//                .join(member.team, team) //inner join : join 되는 대상(teamA)의 멤버만 가져옴
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
//            tuple = [Member(id=1, username=member1, age=10), Team(id=1, name=teamA)]
//            tuple = [Member(id=2, username=member2, age=20), Team(id=1, name=teamA)]
//            tuple = [Member(id=3, username=member3, age=30), null]
//            tuple = [Member(id=4, username=member4, age=40), null]
        }
    }

    //연관관계가 없는 엔티티 외부 조인
    //회원의 이름이 팀 이름과 같은 회원 조회
    @Test
    public void join_on_no_relation() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team) //보통은 member.team , id로 매칭을 안하므로 이름으로만 조인
                .on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void fetchJoinNo() {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isFalse();
    }

    @Test
    public void fetchJoinUse() {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 적용").isTrue();
    }

    //나이가 가장 많은 회원을 조회
    @Test
    public void subQuery() {

        //참조테이블명이 겹치면 안되므로 별도 선언
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                    //static import
                    select(memberSub.age.max())
                            .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(40);
    }

    //나이가 평균 이상인 회원을 조회
    @Test
    public void subQueryGoe() {

        //참조테이블명이 겹치면 안되므로 별도 선언
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                    //static import
                    select(memberSub.age.avg())
                            .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(30, 40);
    }

    //나이가 평균 이상인 회원을 조회
    @Test
    public void subQueryIn() {

        //참조테이블명이 겹치면 안되므로 별도 선언
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                    //static import
                    select(memberSub.age)
                        .from(memberSub)
                        .where(memberSub.age.gt(10))
                        //20, 30, 40
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);
    }

    @Test
    public void selectSubQuery() {

        QMember memberSub = new QMember("memberSub");

        List<Tuple> result = queryFactory
                .select(member.username,
                    //static import
                    select(memberSub.age.avg())
                            .from(memberSub)
                )
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public  void basicCase() {

        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void complexCase() {

        List<String> list = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타")
                )
                .from(member)
                .fetch();

        for (String s : list) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void constant() {
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

    }

    @Test
    public void concat() {

        //username_age
        //나이가 문자열이 아니므로 stringValue로 바꿔줌
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
//                .where(member.username.eq("member1"))
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }

    }

    //프로젝션 대상 1개 (컬럼한개)
    @Test
    public void simpleProjection(){
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    //프로젝션 대상 1개 (전체컬럼)
    @Test
    public void simpleProjection2(){
        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();

        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    //튜플 프로젝션 (컬럼 선택)
    @Test
    public void tupleProjection() {
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }

    //JPQL 에서 DTO 조회
    @Test
    public void findDtoJPQL() {

        //new operation: 생성자 방식만 지원
        List<MemberDto> result = em.createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age) from Member m",
                        MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    //세터 방식
    @Test
    public void findDtoBySetter() {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    //필드 방식
    @Test
    public void findDtoByField() {
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    //생성자 방식
    @Test
    public void findDtoByConstructor() {
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    //필드 방식
    //필드명이 다르면 matching 이 안되서 null
    @Test
    public void findUserDto() {
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        member.age))
                .from(member)
                .fetch();
        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
    }

    //필드 방식
    //서브쿼리 사용하기, 필드명이 없으므로 ExpressionUtils.as() 로 만들어주기
    @Test
    public void findUserDtoWithEx() {

        QMember memberSub = new QMember("memberSub");

        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        ExpressionUtils.as(JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub), "age")
                        )
                )
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
    }

    //생성자 방식
    //필드 이름이 달라도 타입만 맞으면 적용 됨
    @Test
    public void findUserDtoByConstructor() {
        List<UserDto> result = queryFactory
                .select(Projections.constructor(UserDto.class,
                        member.username, member.age))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
    }

    // Projections.constructor : 런타임오류
    // QueryProjection : 컴파일오류
    @Test
    public void findDtoByQueryProjection() {

        //생성자에 QueryProjection 붙여주고 querydsl compile 실행
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1"; //null
        Integer ageParam = 10; //null

        List<Member> result = searchMember1(usernameParam, ageParam);

        assertThat(result.size()).isEqualTo(1);
        //둘다 null 일때 where 절이 안들어감
//        assertThat(result.size()).isEqualTo(4);

    }

    @Test
    public void dynamicQuery_WhereParam() {
        String usernameParam = "member1"; //null
        Integer ageParam = 10; //null

        List<Member> result = searchMember2(usernameParam, ageParam);

        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
                //응답값이 null 이면 무시됨 => 동적쿼리
//                .where(usernameEq(usernameCond), ageEq(ageCond))
                .where(allEq(usernameCond, ageCond))
                .fetch();
    }

    private List<Member> searchMember1(String usernameCond, Integer ageCond) {

        BooleanBuilder builder = new BooleanBuilder();

        if(usernameCond!=null)
            builder.and(member.username.eq(usernameCond));

        if(ageCond!=null)
            builder.and(member.age.eq(ageCond));

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    //where 절 조건 조립 가능

    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond!=null ? member.username.eq(usernameCond) : null;
    }

    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond!=null ? member.age.eq(ageCond) : null;
    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    @Test
    public void bulkUpdate() {

        //member1, member2 바뀌어야함.
        //벌크 연산 시 영속성컨텍스트와 DB의 상태가 달라짐
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();

        //벌크 연산후에는 반드시 영속성컨텍스트 초기화
        em.flush();
        em.clear();

        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();

        for (Member member1 : result) {
            System.out.println("member1 = " + member1);
        }
    }

    @Test
    public void bulkAdd() {
        long count = queryFactory
                .update(member)
//                .set(member.age, member.age.add(1))
                .set(member.age, member.age.multiply(2))
                .execute();

        em.flush();
        em.clear();

        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();

        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void bulkDelete() {
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();

        em.flush();
        em.clear();

        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();

        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void sqlFunction() {
        List<String> result = queryFactory
                //dialect 클래스에 registerFunction 에 등록되어있는 function
                .select(
                        Expressions.stringTemplate(
                                "function('replace', {0}, {1}, {2})",
                                    member.username, "member", "M"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void sqlFunction2() {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .where(member.username.eq(
                        Expressions.stringTemplate("function('lower', {0})",
                                member.username)
                ))
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }
}

