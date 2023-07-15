package study.advanced.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import study.advanced.trace.strategy.code.strategy.ContextV2;
import study.advanced.trace.strategy.code.strategy.StrategyLogic1;
import study.advanced.trace.strategy.code.strategy.StrategyLogic2;

@Slf4j
class ContextV2Test {

    @Test
    public void strategyV1() throws Exception {
        ContextV2 context = new ContextV2();
        context.execute(new StrategyLogic1());
        context.execute(new StrategyLogic2());
    }

    @Test
    public void strategyV2() throws Exception {
        ContextV2 context = new ContextV2();
        context.execute(() -> log.info("비즈니스 로직1 실행"));
        context.execute(() -> log.info("비즈니스 로직2 실행"));
    }

}
