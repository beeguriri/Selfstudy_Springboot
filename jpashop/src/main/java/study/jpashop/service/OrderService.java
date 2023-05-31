package study.jpashop.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.event.spi.DeleteEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.jpashop.domain.Delivery;
import study.jpashop.domain.Member;
import study.jpashop.domain.Order;
import study.jpashop.domain.OrderItem;
import study.jpashop.domain.item.Item;
import study.jpashop.repository.ItemRepository;
import study.jpashop.repository.MemberRepository;
import study.jpashop.repository.OrderRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); //cascade All(order 에서만 사용)

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count); //cascade All(order 에서만 사용)

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    //취소 => 비지니스로직을 도메인에 만들어둠
    public void cancelOrder(Long orderId) {

        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        order.cancel();
    }

    //검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }


}
