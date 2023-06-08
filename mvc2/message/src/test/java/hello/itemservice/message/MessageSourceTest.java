package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource messageSource;

    @Test
    public void helloMessage() throws Exception {

        //getMessage(code, args, locale)
        String result = messageSource.getMessage("hello", null, null);

        assertThat(result).isEqualTo("안녕");
    }

    @Test
    public void notFoundMessageCode() throws Exception {

        //없는 메시지 예외발생 확인
        assertThatThrownBy(() -> messageSource.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    public void notFoundMessageCodeDefaultMessage() throws Exception {

        //기본메시지
        String result = messageSource.getMessage("no_code", null, "기본 메시지", null);

        assertThat(result).isEqualTo("기본 메시지");

    }

    @Test
    public void argumentMessage() {

        //argument 넣어주기
        String result = messageSource.getMessage("hello.name", new Object[]{"spring!"}, null);

        assertThat(result).isEqualTo("안녕 spring!");
    }

    @Test
    void defaultLang() {
        assertThat(messageSource.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(messageSource.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
    }

    @Test
    void enLang(){
        assertThat(messageSource.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }



}
