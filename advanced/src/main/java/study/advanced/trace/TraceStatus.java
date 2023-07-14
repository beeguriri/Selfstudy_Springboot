package study.advanced.trace;

public class TraceStatus {

    private final TraceId traceId;
    private final Long startTimeMS;
    private final String message;

    public TraceStatus(TraceId traceId, Long startTimeMS, String message) {
        this.traceId = traceId;
        this.startTimeMS = startTimeMS;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTimeMS() {
        return startTimeMS;
    }

    public String getMessage() {
        return message;
    }
}
