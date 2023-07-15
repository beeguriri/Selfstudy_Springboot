package study.advanced.trace.logtrace;

import lombok.extern.slf4j.Slf4j;
import study.advanced.trace.TraceId;
import study.advanced.trace.TraceStatus;

@Slf4j
public class ThreadLocalLogTrace implements LogTrace{

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private final ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    // 로그 시작할 때 호출
    private void syncTraceId() {

        TraceId traceId = traceIdHolder.get();
        //최초 호출 시 traceId 새로 만듦
        if(traceId == null)
            traceIdHolder.set(new TraceId());

        // traceId가 있으면 동기화
        else
            traceIdHolder.set(traceIdHolder.get().createNextId());
    }

    // 로그 종료 시 호출
    private void releaseTraceId() {

        TraceId traceId = traceIdHolder.get();

        if(traceId.isFirstLevel())
            traceIdHolder.remove(); // destroy
        else
            traceIdHolder.set(traceIdHolder.get().createPrevId());
    }

    @Override
    public TraceStatus begin(String message) {

        syncTraceId();
        TraceId traceId = traceIdHolder.get();

        long startTimeMs = System.currentTimeMillis();

        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

        return new TraceStatus(traceId, startTimeMs, message);
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e){

        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMS();

        TraceId traceId = status.getTraceId();

        if(e==null)
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        else
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());

        releaseTraceId();
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < level; i++)
            sb.append((i == level - 1) ? "|" + prefix : "|   ");

        return sb.toString();
    }
}
