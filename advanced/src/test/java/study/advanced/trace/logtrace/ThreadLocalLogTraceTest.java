package study.advanced.trace.logtrace;

import org.junit.jupiter.api.Test;
import study.advanced.trace.TraceStatus;

import static org.junit.jupiter.api.Assertions.*;

class ThreadLocalLogTraceTest {

    ThreadLocalLogTrace trace = new ThreadLocalLogTrace();

    @Test
    public void begin_end_level2() throws Exception {

        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello2");
        trace.end(status2);
        trace.end(status1);

    }

    @Test
    public void begin_exception_level2() throws Exception {

        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello2");
        trace.exception(status2, new IllegalStateException());
        trace.exception(status1, new IllegalStateException());

    }
}