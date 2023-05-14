package study.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data", "spring!!");
        return "hello";
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model){
        //url 입력할때 ?name="값" 넣어주면 값이 넘어옴
        model.addAttribute("name", name);

        //뷰리졸버가 resources>templates>hello-template.html 파일 매핑
        //템플릿엔진이 HTML로 변환 후 웹브라우저에 넘겨줌
        return "hello-template";
    }
}
