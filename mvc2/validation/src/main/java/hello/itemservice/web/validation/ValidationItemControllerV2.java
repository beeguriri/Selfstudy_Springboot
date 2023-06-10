package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //필드에러: BindingResult(modelArrtibute에 담긴 object 이름, 필드명, 에러메시지)
        //글로벌에러: BindingResult(modelArrtibute에 담긴 object 이름, 에러메시지)
        //항상 @modelAttribute 뒤에 BindingResult가 와야함! 순서중요!!
        //검증 로직
        //아이템 이름 검증
        if(!StringUtils.hasText(item.getItemName()))
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));

        //가격 입력조건 검증
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000)
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000원 ~ 1,000,000원까지 허용합니다."));

        //수량 검증
        if(item.getQuantity() == null || item.getQuantity() >= 9999)
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용 합니다."));

        // 특정 필드가 아닌 복합 룰 검증 가격*수량의 합 10,000 이상
        if(item.getPrice() != null && item.getQuantity() !=null)
            if(item.getPrice() * item.getQuantity() < 10000)
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + item.getPrice() * item.getQuantity()));

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);

            //@ModelAttribute Item item
            //모델 객체에 Item 자동으로 들어감
            //리다이렉트 해도 모델에 item 담겨 있어서 화면상에서 그대로 보임
            //bindingResult는 modelAttribute에 담겨있음
            return "validation/v2/addForm";
        }

        //검증에 성공하면 수행하는 로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증 로직
        //아이템 이름 검증
        if(!StringUtils.hasText(item.getItemName())) //rejectedValue, bindingFailure, code, args
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));

        //가격 입력조건 검증
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000)
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000원 ~ 1,000,000원까지 허용합니다."));

        //수량 검증
        if(item.getQuantity() == null || item.getQuantity() >= 9999)
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용 합니다."));

        // 특정 필드가 아닌 복합 룰 검증 가격*수량의 합 10,000 이상
        if(item.getPrice() != null && item.getQuantity() !=null)
            if(item.getPrice() * item.getQuantity() < 10000)
                bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + item.getPrice() * item.getQuantity()));

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "validation/v2/addForm";
        }

        //검증에 성공하면 수행하는 로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증 로직
        //아이템 이름 검증
        if(!StringUtils.hasText(item.getItemName())) //rejectedValue, bindingFailure, code, args
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[] {"required.item.itemName"}, null, null));

        //가격 입력조건 검증
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000)
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[] {"range.item.price"}, new Object[]{1000, 1000000}, null));

        //수량 검증
        if(item.getQuantity() == null || item.getQuantity() >= 9999)
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[] {"max.item.quantity"}, new Object[]{9999}, null));

        // 특정 필드가 아닌 복합 룰 검증 가격*수량의 합 10,000 이상
        if(item.getPrice() != null && item.getQuantity() !=null)
            if(item.getPrice() * item.getQuantity() < 10000)
                bindingResult.addError(new ObjectError("item", new String[] {"totalPriceMin"}, new Object[]{10000, item.getPrice() * item.getQuantity()}, null));

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "validation/v2/addForm";
        }

        //검증에 성공하면 수행하는 로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        //검증 로직
        //아이템 이름 검증
        if(!StringUtils.hasText(item.getItemName())) //rejectedValue, bindingFailure, code, args
            //필드명, 에러코드("requied.item.itemName"), args, default message
            bindingResult.rejectValue("itemName", "required");

        //가격 입력조건 검증
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000)
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);

        //수량 검증
        if(item.getQuantity() == null || item.getQuantity() >= 9999)
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);

        // 특정 필드가 아닌 복합 룰 검증 가격*수량의 합 10,000 이상
        if(item.getPrice() != null && item.getQuantity() !=null)
            if(item.getPrice() * item.getQuantity() < 10000)
                bindingResult.reject("totalPriceMin", new Object[]{10000, item.getPrice() * item.getQuantity()}, null);

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "validation/v2/addForm";
        }

        //검증에 성공하면 수행하는 로직

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

