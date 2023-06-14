package study.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class ServletExController {

    @GetMapping("error-ex")
    public void errorEx() {
        throw new RuntimeException("예외 발생");
    }

    @GetMapping("error-400")
    public void error400(HttpServletResponse response) throws IOException {
        response.sendError(400, "404 오류!"); //설정한 message는 default로 안보이게 되있음. 보이게 하려면 별도 옵션 설정
    }

    @GetMapping("error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500);
    }
}
