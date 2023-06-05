package study.jpashop.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.jpashop.domain.Address;
import study.jpashop.domain.Order;
import study.jpashop.domain.OrderStatus;
import study.jpashop.repository.OrderRepository;
import study.jpashop.repository.OrderSearch;
import study.jpashop.repository.order.simplequery.SimpleOrderQueryDto;
import study.jpashop.repository.order.simplequery.SimpleOrderQueryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order
 * Order -> Member  (many to one)
 * Order -> Delivery (one to one)
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final SimpleOrderQueryRepository simpleOrderQueryRepository;

    //1. 엔티티직접노출 하는 방법
    //양방향 연관관계 무한루프 방지 위해 @JsonIgnore
    //hibernate5 모듈 등록해서 해결이 가능하긴 하지만 DTO로 변환해서 반환 하자
    // -> lazy 해결 위해 즉시로딩 (eager) 로 설정하지 말자
    // -> 즉시로딩은 성능 최적화가 어려움. (연관관계 필요없는 경우에도 데이터 항상 조회함)
    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {

        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        //원하는 정보만 Lazy 강제 초기화
        for(Order order: all){
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress();
        }

        return all;
        //무한호출로 인한 무한루프..ㅋㅋ
        //양방향 연관관계에서는 둘중 하나를 jsonignore 해줘야함
        //json ignore 해주고나면 500에러, bytebuddy,... => 지연로딩에서 발생
    }

    //2. 엔티티를 DTO로 변환해서 반환하는 방법
    //lazy loading으로 인한 데이터베이스 호출이 너무 많음
    //member, order, delivery 테이블 세개 호출
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {

        // 결과 2개
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // 루프를 2번 도는데 각각 simpleOrderDto 생성(order, member, delivery 호출)
        // 쿼리 총 5번 나감 (최대 : 1 + N + N)
        List<SimpleOrderDto> result = orders.stream()
                                            .map(o -> new SimpleOrderDto(o))
                                            .collect(Collectors.toList());

        return result;
    }

    //3.성능최적화1 : fetch join
    // => N+1문제 해결 (쿼리 하나만 나감)
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {

        //엔티티로 조회
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;

    }

    //4. 성능 최적화2
    //재사용성은 없음..!
    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderQueryDto> orderV4() {

        //DTO로 조회
        return simpleOrderQueryRepository.findOrderDtos();

    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //Lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //Lazy 초기화
        }
    }

}
