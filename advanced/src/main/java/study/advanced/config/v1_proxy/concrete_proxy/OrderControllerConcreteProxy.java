package study.advanced.config.v1_proxy.concrete_proxy;

import lombok.extern.slf4j.Slf4j;
import study.advanced.proxy.v2.OrderControllerV2;
import study.advanced.trace.TraceStatus;
import study.advanced.trace.logtrace.LogTrace;

@Slf4j
public class OrderControllerConcreteProxy extends OrderControllerV2 {

    private final OrderControllerV2 target;
    private final LogTrace logTrace;

    public OrderControllerConcreteProxy(OrderControllerV2 target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public String request(String itemId) {

        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderController.request()");

            //target 호출
            String result = target.request(itemId);
            logTrace.end(status);

            return result;

        } catch(Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
    }
}
