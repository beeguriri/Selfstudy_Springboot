package study.advanced.config.v1_proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.advanced.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import study.advanced.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import study.advanced.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import study.advanced.proxy.v1.*;
import study.advanced.trace.logtrace.LogTrace;

@Configuration
public class InterfaceProxyConfig {

    //클라이언트는 controller 프록시를 호출
    //controller 프록시는 controller 구현체 호출
    //controller 구현체는 service 프록시 호출
    //service 프록시는 service 구현체 호출
    //service 구현체는 repository 프록시 호출
    //repository 프록시는 repository 구현체 호출

    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace) {
        OrderControllerV1Impl controllerImpl = new OrderControllerV1Impl(orderService(logTrace));
        return new OrderControllerInterfaceProxy(controllerImpl, logTrace);
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace) {
        OrderServiceV1Impl serviceImpl = new OrderServiceV1Impl(orderRepository(logTrace));
        return new OrderServiceInterfaceProxy(serviceImpl, logTrace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
        OrderRepositoryV1Impl repositoryImpl = new OrderRepositoryV1Impl();
        return new OrderRepositoryInterfaceProxy(repositoryImpl, logTrace);
    }
}
