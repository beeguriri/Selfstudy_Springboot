package study.jpashop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.jpashop.domain.item.Item;
import study.jpashop.repository.ItemRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 변경감지 기능 사용
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stock){

        Item findItem = itemRepository.findOne(itemId);

        // 왠만하면 setter 쓰지말고
        // findItem.change(name, price, stock) 같은 메서드 만들어쓰자
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stock);

    } // transactional 에 의해 커밋

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
