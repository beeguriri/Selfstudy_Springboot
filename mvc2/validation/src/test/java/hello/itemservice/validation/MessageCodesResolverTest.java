package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static org.assertj.core.api.Assertions.*;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
//        for(String messageCode : messageCodes)
//            System.out.println("messageCodes = " + messageCode);

        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);//field type까지 넣어줌
//        for(String messageCode : messageCodes)
//            System.out.println("messageCodes = " + messageCode);

        assertThat(messageCodes).containsExactly(
                "required.item.itemName", "required.itemName", "required.java.lang.String", "required");

    }

}
