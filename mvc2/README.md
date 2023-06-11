[**1. 타임리프 - 기본 기능**](https://github.com/beeguriri/Selfstudy_Springboot/tree/main/mvc2/thymeleaf-basic)

**2. 타임리프 - 스프링 통합과 폼**

[**3. 메시지, 국제화**](https://github.com/beeguriri/Selfstudy_Springboot/tree/main/mvc2/message)
- 메시지 기능
  - HTML 파일에 하드코딩 된 메시지('상품명')를 
  - `messages.properties` 라는 파일을 만들어 한곳에서 관리
```html
 <label for="itemName">상품명</label>
```
- 스프링부트 기본 기능
```java
//application.properties
spring.messages.basename=messages

//messages.properties 
관리할 메시지 작성
```
- 타임리프에서 `th:text="#{page.items}"` 로 사용
  - 파라미터로 쓸 때는 `th:text="#{hello.name(${item.itemName})}"`
- 국제화
  - 메시지 파일을 각 나라 별로 별도 관리
  - `messages_en.properties` 등으로 파일 생성 해두면 됨

[**4. 검증 - Validation**](https://github.com/beeguriri/Selfstudy_Springboot/tree/main/mvc2/validation)
- 의존관계 추가
```java
implementation 'org.springframework.boot:spring-boot-starter-validation'
```
- [validation 관련 어노테이션](https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/#validator-defineconstraints-spec)
- 스프링부트는 자동으로 글로벌 Validator를 등록하며, `@Validated`를 붙여서 사용
- 검증 에러가 발생하면 bindingResult에 담아줌
  - `bindingResult`: 스프링이 제공하는 검증 오류를 보관하는 객체, `순서중요`
```java
//ModelAttibute
public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, ...

//RequestBody
public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult){ ...
```
- 검증순서
  - `@ModelAttibute`: HPPT 요청 파라미터(URL, 쿼리 스트링, POST Form)를 다룰 때 사용
    - 각각의 필드에 타입 변환 시도, binding 성공한 필드만 validation 적용
    - 실패하면 'typeMismatch', 'FieldError' 추가하고 validation 적용 안됨
  - `@RequestBody`: HTTP Body의 데이터를 객체로 변환할 때, 주로 API JSON 요청 다룰때 사용
    - 객체 단위로 적용되므로 `HttpMessageConverter`단계에서 json binding이 안되면 
    - controller 호출 자체가 안되므로, validation 적용이 안됨
- (참고) 동일 모델 객체를 각각 다르게 검증해야할 필요가 있으므로 각각의 폼 전송객체 만들어서 사용하자!

**6. 로그인 처리1 - 쿠키, 세션**

**7. 로그인 처리2 - 필터, 인터셉터**

**8. 예외 처리와 오류 페이지**

**9. API 예외 처리**

**10. 스프링 타입 컨버터**

**11. 파일 업로드**
