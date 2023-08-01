package study.advanced.config.v3_proxyfactory.advice;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import study.advanced.trace.TraceStatus;
import study.advanced.trace.logtrace.LogTrace;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class LogTraceAdvice implements MethodInterceptor {

    private final LogTrace logTrace;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        TraceStatus status = null;

        try {
            Method method = invocation.getMethod();

            //동적으로 메시지 작성
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = logTrace.begin(message);

            //로직 호출
            Object result = invocation.proceed();
            logTrace.end(status);

            return result;

        } catch(Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
