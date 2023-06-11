package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(lang = "javascript",
//        script = "_this.price * _this.quantity >= 10000",
//        message = "총 합이 10,000원 넘게 입력해주세요.")
public class Item {

    private Long id;

    //빈값 + 공백만 있는 경우 허용 안함
    //메시지 설정도 가능
    @NotBlank(message = "공백X")
    private String itemName;

    @NotNull //null 허용 안함
    @Range(min=1000, max=1000000) //하이버네이트 validator 구현체 사용할때만 제공
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
