package beeguri.securitystudy.controller;

import beeguri.securitystudy.domain.Member;
import beeguri.securitystudy.dto.MemberJoinDto;
import beeguri.securitystudy.dto.MemberLoginDto;
import beeguri.securitystudy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {

    @Autowired
    MemberService memberService;

    @PostMapping("/api/login")
    public Member login(@RequestBody MemberLoginDto params){
        return memberService.login(params);
    }

    @GetMapping("/api/logout")
    public Member logout() {
        return null;
    }

    @PostMapping("/api/join")
    public void join(@RequestBody MemberJoinDto params) {
        memberService.createMember(params);
    }

    @GetMapping("/api/test")
    public @ResponseBody String test (){
        return "test";
    }

}
