package study.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("hello-string")
    @ResponseBody //응답 body 에 return 부분이 직접 들어감 (뷰 없음)
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

    @GetMapping("hello-api")
    @ResponseBody
    // ResponseBody: http 바디에 바로 데이터 반환(객체 반환하면 => default 가 json!)
    // httpMessageConverter 가 동작
    // 기본문자 : StringHttpMessageConverter
    // 기본객체 : MappingJackson2HttpMessageConverter
    public Hello helloApi(@RequestParam("name") String name){
        Hello hello = new Hello();
        hello.setName(name);

        //json 형태로 return (key: "name":, value: name)
        return hello;
    }

    static class Hello {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
