package study.advanced.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//프록시도 실제 객체와 모양이 같아야하므로 `Subject`인터페이스 구현
public class CacheProxy implements Subject{

    private final Subject target;
    private String cacheValue;

    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {

        log.info("프록시 호출");

        //첫번째 호출이면(null) 실제 객체 호출
        //두번째 호출부터는 데이터 바로 반환
        if (cacheValue == null)
            cacheValue = target.operation();

        return cacheValue;
    }
}
