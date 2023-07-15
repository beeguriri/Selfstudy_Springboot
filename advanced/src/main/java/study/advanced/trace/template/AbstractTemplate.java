package study.advanced.trace.template;

import lombok.RequiredArgsConstructor;
import study.advanced.trace.TraceStatus;
import study.advanced.trace.logtrace.LogTrace;

@RequiredArgsConstructor
public abstract class AbstractTemplate<T> {

    private final LogTrace trace;

    public T execute(String message) {
        TraceStatus status = null;
        try{
            status = trace.begin(message);

            //로직 호출(추상화)
            T result = call();

            trace.end(status);
            return result;
            
        } catch(Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    //상속으로 구현할 것
    protected abstract T call();
}
