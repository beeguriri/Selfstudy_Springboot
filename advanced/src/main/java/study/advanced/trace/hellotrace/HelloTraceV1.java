package study.advanced.trace.hellotrace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import study.advanced.trace.TraceId;
import study.advanced.trace.TraceStatus;

@Slf4j
@Component
public class HelloTraceV1 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    //로그 시작 [796bccd9] OrderController.request()
    public TraceStatus begin(String message){
        TraceId traceId = new TraceId();
        long startTimeMs = System.currentTimeMillis();

        //로그 출력
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

        return new TraceStatus(traceId, startTimeMs, message);
    };

    //로그 종료 - 정상 종료 [796bccd9] OrderController.request() time=1016ms
    public void end(TraceStatus status){
        complete(status, null);
    };

    //로그 종료 - 예외 종료  [b7119f27] |   |<X-OrderRepository.save() time=0ms ex=java.lang.IllegalStateException: 예외 발생!
    public void exception(TraceStatus status, Exception e){
        complete(status, e);
    };

    private void complete(TraceStatus status, Exception e){

        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMS();

        TraceId traceId = status.getTraceId();

        if(e==null)
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        else
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
    }

    // START_PREFIX = "-->";
    // COMPLETE_PREFIX = "<--";
    // EX_PREFIX = "<X-";
    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < level; i++)
            sb.append((i == level - 1) ? "|" + prefix : "|   ");

        return sb.toString();
    }
}
