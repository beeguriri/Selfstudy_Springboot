package study.hellospring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import study.hellospring.service.MemberService;

@Controller
public class MemberController {

    private final MemberService memberService;

    //스프링 컨테이너에 등록하고 사용하기 위해
    //MemberService 도 컨테이너에 등록되어있어야 함
    //생성자를 통해 의존관계 주입
    @Autowired
    public MemberController(MemberService memberService) {

        this.memberService = memberService;
    }
}
