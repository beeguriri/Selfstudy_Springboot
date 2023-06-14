package study.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        log.info("exception", ex);

        try {
            if (ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException resolver to 400");

                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage()); //예외를 먹고 서버에 400이라고 전달

                return new ModelAndView();
            }
        }catch (IOException e){
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return null; //예외가 다시 날라감
    }
}
