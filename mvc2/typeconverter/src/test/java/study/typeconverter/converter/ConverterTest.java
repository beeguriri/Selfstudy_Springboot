package study.typeconverter.converter;

import org.junit.jupiter.api.Test;
import study.typeconverter.type.IpPort;

import static org.assertj.core.api.Assertions.*;

public class ConverterTest {

    @Test
    public void stringToInteger() throws Exception {
        //given
        StringToIntegerConverter converter = new StringToIntegerConverter();

        //when
        Integer result = converter.convert("10");

        //then
        assertThat(result).isEqualTo(10);
    }
    
    @Test
    public void integerToString() throws Exception {
        //given
        IntegerToStringConverter converter = new IntegerToStringConverter();

        //when
        String result = converter.convert(10);

        //then
        assertThat(result).isEqualTo("10");
    }
    
    @Test
    public void stringToIpPort() throws Exception {
        //given
        IpPortToStringConverter converter = new IpPortToStringConverter();

        //when
        IpPort source = new IpPort("127.0.0.1", 8080);
        String result = converter.convert(source);

        //then
        assertThat(result).isEqualTo("127.0.0.1:8080");
    }

    @Test
    public void ipPortToString() throws Exception {
        //given
        StringToIpPortConverter converter = new StringToIpPortConverter();
        String source = "127.0.0.1:8080";

        //when
        IpPort result = converter.convert(source);

        //then
        //@EqualsAndHashCode : 참조값이 달라도 데이터가 같으면 true
        assertThat(result).isEqualTo(new IpPort("127.0.0.1", 8080));
    }
}
