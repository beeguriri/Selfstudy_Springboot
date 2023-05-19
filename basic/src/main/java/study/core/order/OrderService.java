package study.core.order;

public interface OrderService {

    //회원 아이디, 상품명, 상품가격으로 주문을 신규 생성하면
    //주문 결과를 리턴한다
    Order createOrder(Long memberId, String itemName, int itemPrice);

}
