package study.core.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import study.core.discount.FixDiscountPolicy;
import study.core.member.Grade;
import study.core.member.Member;
import study.core.member.MemoryMemberRepository;

class OrderServiceImplTest {

    @Test
    void createOrder() {

        MemoryMemberRepository memberRepository = new MemoryMemberRepository();
        memberRepository.save(new Member(1L, "memberA", Grade.VIP));
        OrderServiceImpl orderService = new OrderServiceImpl(memberRepository, new FixDiscountPolicy());
        Order order = orderService.createOrder(1L, "itemA", 10000);

        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }

}