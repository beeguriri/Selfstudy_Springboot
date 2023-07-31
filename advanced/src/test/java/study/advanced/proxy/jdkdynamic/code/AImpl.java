package study.advanced.proxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AImpl implements AInterface{
    @Override
    public String call() {
        log.info("A호출");
        return "a";
    }
}
