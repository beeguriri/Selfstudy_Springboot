package study.jpashop.api;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.jpashop.domain.Address;
import study.jpashop.domain.Order;
import study.jpashop.domain.OrderItem;
import study.jpashop.domain.OrderStatus;
import study.jpashop.repository.OrderRepository;
import study.jpashop.repository.OrderSearch;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order
 * Order -> OrderItem (one to many) -> Item(one to one)
 */

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    //v1. 엔티티 직접 노출
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){

        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        //Lazy 강제 초기화
        for (Order order : all) {

            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();

            orderItems.stream().forEach(o -> o.getItem().getName());

        }

        return all;
    }

    //v2. dto 만들기
    //dto 안에 orderItem Entity도 풀어서 dto로 만들어줌
    //쿼리 : order -> (member -> delivery -> orderItem -> item*2번) * 2번
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){

        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        List<OrderDto> result = orders.stream()
                                        .map(o -> new OrderDto(o))
                                        .collect(Collectors.toList());

        return result;
    }

    //v3. fetch join
    //distinct keyword: jpa 에서 자체적으로 order 가 같은 id 값이면 중복을 제거하고 컬렉션에 담아줌
    //단점: oneToMany join 시 페이징 불가능해짐
    //oneToMany 에서 one 을 기준으로 페이징을 하는 것이 목적이나, many 를 기준으로 row 가 생성됨.
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){

        List<Order> orders = orderRepository.findAllWithItem();

        return orders.stream()
                        .map(o -> new OrderDto(o))
                        .collect(Collectors.toList());
    }

    //v3.1 페이징 기능 구현
    //default_batch_fetch_size 설정으로 컬렉션의 N+1 해결!
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit){

        //x to one 은 fetch join 으로 가져오기 (paging 에 영향을 주지 않음)
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        //default_batch_fetch_size 설정하면 In 쿼리를 날려줌
        //size : In 쿼리 개수
        //order_id in (4, 11);
        //item_id in (2, 3, 9, 10);
        return orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
    }

    @Data
    static class OrderDto{

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
//        private List<OrderItem> orderItems; // dto 안에 orderItem Entity 들어있음
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
//            order.getOrderItems().stream().forEach(o -> o.getItem().getName()); //Lazy 초기화
            orderItems = order.getOrderItems().stream()
                                .map(orderItem -> new OrderItemDto(orderItem))
                                .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto{

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

}
