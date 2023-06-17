## 스프링 타입 컨버터
- Http 요청 파라미터는 모두 문자로 처리됨
- 스프링의 타입컨버터 (@RequestParam, @PathVariable, @ModelAttribute)

### 컨버터 인터페이스 구현
- implements Converter<S, T>
  - S : 파라미터, T : 반환값

### ConversionService 인터페이스 구현
- 스프링은 숫자, 문자, boolean 등 일반적인 타입에 대한 컨버터 기본 제공
- 컨버팅이 가능한지 확인하는 기능, 컨버팅 기능 제공
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    //컨버터 추가
    @Override
    public void addFormatters(FormatterRegistry registry) {
      registry.addConverter(new StringToIntegerConverter());
      registry.addConverter(new IntegerToStringConverter());
      registry.addConverter(new StringToIpPortConverter());
      registry.addConverter(new IpPortToStringConverter());
    }
}
```
- [참고] **인터페이스 분리원칙** : 클라이언트는 자신이 사용하지않는 메서드에 의존하지 않아야한다.
  - 스프링의 DefaultConversionService는 `ConversionService`와 `ConversionRegistry`를 구현한 것

### Formatter
- Converter: 객체 -> 객체
- Formatter: 객체 -> 문자, 문자 -> 객체 + 현지화(Locale)
  - 객체를 문자로 변경: String print(T object, Locale locale)
  - 문자를 객체로 변경: T parse(String text, Locale locale)

### FormattingConversionServie
- 포매팅을 지원하는 컨버전 서비스
- DefaultFormattingConversionServie : 통화, 숫자 관련 기본 포맷터 제공

### 어노테이션 포맷
- 숫자관련: `@NumberFormat(pattern = "###,###")`
- 날짜관련: `@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")`