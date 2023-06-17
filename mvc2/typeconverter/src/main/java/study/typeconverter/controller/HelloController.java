package study.typeconverter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.typeconverter.type.IpPort;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {

        //Http 요청 파라미터는 모두 문자로 처리됨
        String data = request.getParameter("data"); //문자 타입 조회
        Integer intValue = Integer.valueOf(data); //숫자 타입으로 변경

        System.out.println("intValue = " + intValue);

        return "ok";
    }

    @GetMapping("hello-v2")
    //스프링이 중간에서 타입을 변환해줌
    //s.t.converter.StringToIntegerConverter   : convert source=10
    public String helloV2(@RequestParam Integer data) {
        System.out.println("data = " + data);

        return "ok";
    }

    @GetMapping("ip-port")
    public String ipPort(@RequestParam IpPort ipPort){
        System.out.println("ipPort IP = " + ipPort.getIp());
        System.out.println("ipPort Port = " + ipPort.getPort());

        return "ok";
    }
}
