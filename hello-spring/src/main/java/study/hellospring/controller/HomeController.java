package study.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    //먼저 스프링컨테이너에 관련 컨트롤러가 있는지 먼저 찾고
    //없으면 static 의 index.html 띄워줌
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
