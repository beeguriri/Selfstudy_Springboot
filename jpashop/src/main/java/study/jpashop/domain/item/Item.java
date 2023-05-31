package study.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;
import study.jpashop.domain.Category;
import study.jpashop.exception.NotEnoughStockException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //상속관계 매핑, 한테이블에 다 넣어서 만듦
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    //상속관계 매핑
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비지니스 로직 : 도메인 주도 설계==//
    //재고 증가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    //재고 감소
    public void removeStock(int quantity) {
        int resultStock = this.stockQuantity - quantity;
        if(resultStock < 0)
            throw new NotEnoughStockException("need more stock");

        this.stockQuantity = resultStock;

    }

}
