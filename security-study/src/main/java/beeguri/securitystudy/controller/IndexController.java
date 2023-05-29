//package beeguri.securitystudy.controller;
//
//import beeguri.securitystudy.dto.MemberJoinDto;
//import beeguri.securitystudy.service.MemberService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//public class IndexController {
//
//    @Autowired
//    MemberService memberService;
//
//    //로그인 한 사람만 접근
//    @GetMapping({"", "/"})
//    public @ResponseBody String index() {
//        return "인덱스 페이지";
//    }
//
//    //로그인 한 사람만 접근
//    @GetMapping("/user")
//    public @ResponseBody String user() {
//        return "user";
//    }
//
//    //로그인 한 사람 중 권한 admin
//    @GetMapping("/admin")
//    public @ResponseBody String admin() {
//        return "admin";
//    }
//
//    //로그인 한 사람 중 권한 manager
//    @GetMapping("/manager")
//    public @ResponseBody String manager() {
//        return "manager";
//    }
//
//    @GetMapping("/loginForm")
//public                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       @ResponseBody String login() {
//        return "loginForm";
//    }
//
//    @PostMapping("/joinTest")
//    public String join(MemberJoinDto params) {
//
//        memberService.createMember(params);
//        return "redirect:/";
//
//    }
//
//    @GetMapping("/joinProc")
//    public @ResponseBody String joinProc() {
//        return "회원가입 완료 됨";
//    }
//
//    @Secured("ROLE_ADMIN")
//    @GetMapping("/info")
//    public @ResponseBody String info() {
//        return "개인정보";
//    }
//
//    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //메서드 실행전에 활성화
//    @GetMapping("/data")
//    public @ResponseBody String data() {
//        return "데이터 정보";
//    }
//}
