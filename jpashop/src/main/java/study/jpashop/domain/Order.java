package study.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    //1:n의 관계에서는 n 쪽이 주인! (order에 있는 member를 바꿈)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY) //order에 access를 더 많이 하므로 fk를 order에 둠
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    // 자바8 이상에서는 하이버네이트가 자동 지원함
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private orderStatus status; // 주문상태 [order, cancel]
}

