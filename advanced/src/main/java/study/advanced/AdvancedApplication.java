package study.advanced;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import study.advanced.config.v2_dynamicproxy.DynamicProxyBasicConfig;
import study.advanced.trace.logtrace.LogTrace;
import study.advanced.trace.logtrace.ThreadLocalLogTrace;

//@Import({AppV1Config.class, AppV2Config.class})
//@Import(InterfaceProxyConfig.class)
//@Import(ConcreteProxyConfig.class)
@Import(DynamicProxyBasicConfig.class)
@SpringBootApplication(scanBasePackages = "study.advanced.proxy")
public class AdvancedApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvancedApplication.class, args);
	}

	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalLogTrace();
	}

}
