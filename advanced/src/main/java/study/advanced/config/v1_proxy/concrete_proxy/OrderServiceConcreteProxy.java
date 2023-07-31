package study.advanced.config.v1_proxy.concrete_proxy;

import lombok.extern.slf4j.Slf4j;
import study.advanced.proxy.v2.OrderServiceV2;
import study.advanced.trace.TraceStatus;
import study.advanced.trace.logtrace.LogTrace;

@Slf4j
public class OrderServiceConcreteProxy extends OrderServiceV2 {

    private final OrderServiceV2 target;
    private final LogTrace logTrace;

    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {
        //부모의 생성자를 호출해야하기 때문에
        //자바의 문법상 넣어줌. 실제로는 프록시로 사용할 거기땜에 null 넣어줌
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderService.orderItem()");

            //target 호출
            target.orderItem(itemId);
            logTrace.end(status);

        } catch(Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
