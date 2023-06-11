package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(lang = "javascript",
//        script = "_this.price * _this.quantity >= 10000",
//        message = "총 합이 10,000원 넘게 입력해주세요.")
public class Item3 {

    @NotNull(groups = UpdateCheck.class) // 수정할때 반드시 있어야함
    private Long id;

    //빈값 + 공백만 있는 경우 허용 안함
    //메시지 설정도 가능
    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class}) //null 허용 안함
    @Range(min=1000, max=1000000, groups = {SaveCheck.class, UpdateCheck.class}) //하이버네이트 validator 구현체 사용할때만 제공
    private Integer price;

    @NotNull
    @Max(value = 9999, groups = {SaveCheck.class}) //수정할때는 무제한
    private Integer quantity;

    public Item3() {
    }

    public Item3(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
