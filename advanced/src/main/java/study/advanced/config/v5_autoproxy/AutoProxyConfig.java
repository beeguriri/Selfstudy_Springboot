package study.advanced.config.v5_autoproxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import study.advanced.config.AppV1Config;
import study.advanced.config.AppV2Config;
import study.advanced.config.v3_proxyfactory.advice.LogTraceAdvice;
import study.advanced.trace.logtrace.LogTrace;

@Slf4j
@Import({AppV1Config.class, AppV2Config.class})
@Configuration
public class AutoProxyConfig {

    @Bean
    public Advisor advisor1(LogTrace logTrace) {

        //포인트컷
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        //어드바이스
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
