package study.advanced.proxy.pureproxy.decorator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import study.advanced.proxy.pureproxy.decorator.code.Component;
import study.advanced.proxy.pureproxy.decorator.code.DecoratorPatternClient;
import study.advanced.proxy.pureproxy.decorator.code.MessageDecorator;
import study.advanced.proxy.pureproxy.decorator.code.RealComponent;

@Slf4j
public class DecoratorPatternTest {

    @Test
    void noDecorator() {

        RealComponent component = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(component);

        client.execute();
    }

    @Test
    void decorator1() {

        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(messageDecorator);

        client.execute();
    }
}
