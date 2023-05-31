package study.jpashop.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import study.jpashop.domain.Address;
import study.jpashop.domain.Member;
import study.jpashop.service.MemberService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    //@Valid : MemberForm에 있는 validation을 사용하겠다
    //BindingResult : 오류가 담겨서 코드가 실행 됨
    public String create(@Valid MemberForm form, BindingResult result) {

        // error가 있으면 => form으로 다시 가겠다!
        if (result.hasErrors())
            return "members/createMemberForm";

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {

        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/memberList";
    }
}
