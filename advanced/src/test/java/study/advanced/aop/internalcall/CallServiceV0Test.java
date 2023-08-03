package study.advanced.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import study.advanced.aop.internalcall.aop.CallLogAspect;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV0Test {

    @Autowired
    CallServiceV0 callServiceV0;

    @Test
    void external() {
        log.info("target={}", callServiceV0.getClass());
        callServiceV0.external();
        //internal() aop 적용 안 됨
    }

    @Test
    void internal() {
        //aop 적용 됨
        log.info("target={}", callServiceV0.getClass());
        callServiceV0.internal();
    }

}