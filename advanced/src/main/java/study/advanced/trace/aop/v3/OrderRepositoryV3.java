package study.advanced.trace.aop.v3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.advanced.trace.TraceStatus;
import study.advanced.trace.logtrace.LogTrace;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV3 {

    private final LogTrace trace;

    public void save(String itemId){

        TraceStatus status = null;

        //예외가 발생해도 log 출력
        try {
            status = trace.begin("OrderRepository.save()");

            if(itemId.equals("ex"))
                throw new IllegalStateException("예외 발생");

            sleep(1000);

            trace.end(status);

        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
