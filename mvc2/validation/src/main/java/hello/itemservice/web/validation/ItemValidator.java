package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        //자식 클래스까지 커버
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Item item = (Item) target;

        //검증 로직
        //아이템 이름 검증
        if(!StringUtils.hasText(item.getItemName()))
            errors.rejectValue("itemName", "required");

        //가격 입력조건 검증
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000)
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);

        //수량 검증
        if(item.getQuantity() == null || item.getQuantity() >= 9999)
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);

        // 특정 필드가 아닌 복합 룰 검증 가격*수량의 합 10,000 이상
        if(item.getPrice() != null && item.getQuantity() !=null)
            if(item.getPrice() * item.getQuantity() < 10000)
                errors.reject("totalPriceMin", new Object[]{10000, item.getPrice() * item.getQuantity()}, null);

    }
}
