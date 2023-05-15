package study.hellospring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.hellospring.repository.JdbcMemberRepository;
import study.hellospring.repository.JdbcTempleteMemberRepository;
import study.hellospring.repository.MemberRepository;
import study.hellospring.repository.MemoryMemberRepository;
import study.hellospring.service.MemberService;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private DataSource dataSource;

    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
        return new JdbcTempleteMemberRepository(dataSource);
    }
}
