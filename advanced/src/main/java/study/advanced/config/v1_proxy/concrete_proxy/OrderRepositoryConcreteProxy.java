package study.advanced.config.v1_proxy.concrete_proxy;

import lombok.RequiredArgsConstructor;
import study.advanced.proxy.v2.OrderRepositoryV2;
import study.advanced.trace.TraceStatus;
import study.advanced.trace.logtrace.LogTrace;

@RequiredArgsConstructor
public class OrderRepositoryConcreteProxy extends OrderRepositoryV2 {

    private final OrderRepositoryV2 target;
    private final LogTrace logTrace;

    @Override
    public void save(String itemId) {
        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderRepository.save()");
            //target 호출
            target.save(itemId);
            logTrace.end(status);
        } catch(Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
