package study.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        // test 를 위해 @Configuration 붙은 거 컴포넌트 스캔 제외
        excludeFilters = @ComponentScan.Filter(type= FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
}
