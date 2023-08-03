package study.advanced.aop.proxyvs;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import study.advanced.aop.member.MemberService;
import study.advanced.aop.member.MemberServiceImpl;

@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false); //JDK 동적 프록시

        //프록시를 인터페이스로 캐스팅 => 성공
        //JDK 자체가 인터페이스 기반으로 만들어지므로
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        //구체 타입으로 캐스팅은 불가
        //ClassCastException 예외 발생
        Assertions.assertThrows(ClassCastException.class, () -> {
            MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
        });
    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); //CGLIB 프록시

        //프록시를 인터페이스로 캐스팅 => 성공
        //JDK 자체가 인터페이스 기반으로 만들어지므로
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        //구체 타입으로 캐스팅 => 성공
        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
    }
}
