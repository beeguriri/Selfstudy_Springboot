package study.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //생성메서드에 따라 생성하라고 제약(기본생성자 호출 금지)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    //1:n의 관계에서는 n 쪽이 주인! (order에 있는 member를 바꿈)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    // order와 orderItems를 각각 저장해주어야  하나 (엔티티 당 persist 각각 호출)
    // cascade => All 일 경우 order만 저장해주면 자동으로 orderItems 저장
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) //order에 access를 더 많이 하므로 fk를 order에 둠
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    // 자바8 이상에서는 하이버네이트가 자동 지원함
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private orderStatus status; // 주문상태 [order, cancel]

    // 양방향일때 연관관계 메서드 //
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){

        Order order = new Order();

        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems)
            order.addOrderItem(orderItem);

        order.setStatus(orderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    //==비지니스 로직==//
    //주문취소
    public void cancel() {

        if (delivery.getStatus() == DeliveryStatus.COMP)
            throw new IllegalStateException("이미 배송 완료 된 상품은 취소가 불가능 합니다.");

        this.setStatus(orderStatus.CANCEL);

        // 재고 원복
        for (OrderItem orderItem : orderItems)
            orderItem.cancel();

    }

    //==조회 로직==//
    public int getTotalPrice() {

        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();

//        int totalPrice = 0;
//        for (OrderItem orderItem : orderItems)
//            totalPrice += orderItem.getTotalPrice();
//
//        return totalPrice;
    }


}

