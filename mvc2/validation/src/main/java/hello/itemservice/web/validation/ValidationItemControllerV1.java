package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {

        //검증 오류 결과 보관
        Map<String, String> errors = new HashMap<>();

        //검증 로직
        //아이템 이름 검증
        if(!StringUtils.hasText(item.getItemName()))
            errors.put("itemName", "상품 이름은 필수입니다.");

        //가격 입력조건 검증
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000)
            errors.put("price", "가격은 1,000원 ~ 1,000,000원까지 허용합니다.");

        //수량 검증
        if(item.getQuantity() == null || item.getQuantity() >= 9999)
            errors.put("quantity", "수량은 최대 9,999 까지 허용 합니다.");

        // 특정 필드가 아닌 복합 룰 검증 가격*수량의 합 10,000 이상
        if(item.getPrice() != null && item.getQuantity() !=null)
            if(item.getPrice() * item.getQuantity() < 10000)
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + item.getPrice() * item.getQuantity());

        //검증에 실패하면 다시 입력 폼으로
        if(!errors.isEmpty()){
            log.info("errors={}", errors);
            model.addAttribute("errors", errors);

            //@ModelAttribute Item item
            //모델 객체에 Item 자동으로 들어감
            //리다이렉트 해도 모델에 item 담겨 있어서 화면상에서 그대로 보임

            return "validation/v1/addForm";
        }

        //검증에 성공하면 수행하는 로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }

}

