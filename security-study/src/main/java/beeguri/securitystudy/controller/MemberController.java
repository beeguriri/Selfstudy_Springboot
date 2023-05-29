package beeguri.securitystudy.controller;

import beeguri.securitystudy.domain.Member;
import beeguri.securitystudy.dto.MemberLoginDto;
import beeguri.securitystudy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class MemberController {

    @Autowired
    MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<Member> login(@RequestBody MemberLoginDto params){
        return ResponseEntity.ok().body(memberService.login(params));
    }

    @GetMapping("/logout")
    public ResponseEntity<Member> logout(@RequestBody MemberLoginDto params){
        return ResponseEntity.ok().body(memberService.logout(params));
    }

}
