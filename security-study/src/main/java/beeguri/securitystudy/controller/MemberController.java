package beeguri.securitystudy.controller;

import beeguri.securitystudy.domain.Member;
import beeguri.securitystudy.dto.MemberLoginDto;
import beeguri.securitystudy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @Autowired
    MemberService memberService;

    @PostMapping("/api/login")
    public Member login(@RequestBody MemberLoginDto params){
        return memberService.login(params);
    }

}
