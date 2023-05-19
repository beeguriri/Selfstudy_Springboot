package study.core.order;

import study.core.discount.DiscountPolicy;
import study.core.discount.FixDiscountPolicy;
import study.core.member.Member;
import study.core.member.MemberRepository;
import study.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        //단일 책임 원칙을 지키는 설계
        //OrderService는 할인에 대한것과 관계 없음
        //할인은 DiscountPolicy만 관계 있음

        //주문생성 요청이 오면 회원을 찾고, 할인금액을 계산해서
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        //주문 결과를 리턴
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
