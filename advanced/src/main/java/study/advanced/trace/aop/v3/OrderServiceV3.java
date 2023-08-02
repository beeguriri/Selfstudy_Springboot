package study.advanced.trace.aop.v3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.advanced.trace.TraceStatus;
import study.advanced.trace.logtrace.LogTrace;

@Service
@RequiredArgsConstructor
public class OrderServiceV3 {

    private final OrderRepositoryV3 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {

        TraceStatus status = null;

        //예외가 발생해도 log 출력
        try {
            status = trace.begin("OrderService.orderItem()");
            orderRepository.save(itemId);
            trace.end(status);

        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
