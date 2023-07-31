package study.advanced.config.v2_dynamicproxy.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.util.PatternMatchUtils;
import study.advanced.trace.TraceStatus;
import study.advanced.trace.logtrace.LogTrace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;
    private final String[] patterns;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //메서드 이름 필터
        String methodName = method.getName();
        if(!PatternMatchUtils.simpleMatch(patterns, methodName))
            return method.invoke(target, args);

        TraceStatus status = null;

        try {
            //동적으로 메시지 작성
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = logTrace.begin(message);

            //로직 호출
            Object result = method.invoke(target, args);
            logTrace.end(status);

            return result;

        } catch(Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
