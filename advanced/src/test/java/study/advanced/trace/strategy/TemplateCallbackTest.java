package study.advanced.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import study.advanced.trace.strategy.code.template.Callback;
import study.advanced.trace.strategy.code.template.TimeLogTemplate;

@Slf4j
public class TemplateCallbackTest {

    /*
    템플릿 콜백 패턴 - 익명내부클래스
    코드조각 넘김
     */
    @Test
    public void callbackV1() throws Exception {
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직 1 실행");
            }
        });
        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직 2 실행");
            }
        });
    }

    /*
    템플릿 콜백 패턴 - 람다
    코드조각 넘김
     */
    @Test
    public void callbackV2() throws Exception {
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(() -> log.info("비즈니스 로직 1 실행"));
        template.execute(() -> log.info("비즈니스 로직 2 실행"));
    }
}
