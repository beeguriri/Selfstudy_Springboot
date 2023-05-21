package study.core.order;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import study.core.annotation.MainDiscountPolicy;
import study.core.discount.DiscountPolicy;
import study.core.discount.FixDiscountPolicy;
import study.core.discount.RateDiscountPolicy;
import study.core.member.Member;
import study.core.member.MemberRepository;
import study.core.member.MemoryMemberRepository;

@Component
//@RequiredArgsConstructor //final이 붙은 필드로 생성자를 만들어줌
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

//    @Autowired //세터주입 테스트
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//
//    @Autowired //세터주입 테스트
//    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
//        this.discountPolicy = discountPolicy;
//    }

    @Autowired
//    public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
//    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

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

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
