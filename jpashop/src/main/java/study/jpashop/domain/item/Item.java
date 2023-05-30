package study.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;
import study.jpashop.domain.Category;

import javax.persistence.*;
import java.nio.MappedByteBuffer;
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

}
