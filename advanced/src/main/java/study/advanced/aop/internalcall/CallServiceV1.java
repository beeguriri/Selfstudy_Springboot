package study.advanced.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV1 {

    //자기자신은 생성자 주입 불가 => 순환참조
    private CallServiceV1 callServiceV1;

    //세터주입으로 변경해 줌
    //callServiceV1 setter=class study.advanced.aop.internalcall.CallServiceV1$$EnhancerBySpringCGLIB$$7f54fa65
    @Autowired
    public void setCallServiceV1(CallServiceV1 callServiceV1) {
        log.info("callServiceV1 setter={}", callServiceV1.getClass());
        this.callServiceV1 = callServiceV1;
    }

    public void external() {
        log.info("call external");
        callServiceV1.internal(); //내부 메서드 호출 (this.internal())
    }

    public void internal() {
        log.info("call internal");
    }
}
