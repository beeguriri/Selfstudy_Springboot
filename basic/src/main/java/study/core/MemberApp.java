package study.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import study.core.member.Grade;
import study.core.member.Member;
import study.core.member.MemberService;
import study.core.member.MemberServiceImpl;

public class MemberApp {

    public static void main(String[] args) {

//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = new MemberServiceImpl();
//        MemberService memberService = appConfig.memberService();

        // Appconfig의 설정정보를 가지고 (@bean 어노테이션 붙은)
        // 스프링 컨테이너에 넣어서 관리 해줌
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        System.out.println("new Member : " + member.getName());
        System.out.println("find Member : " + findMember.getName());
    }
}
