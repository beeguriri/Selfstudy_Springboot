## [1. 타임리프 - 기본 기능](https://github.com/beeguriri/Selfstudy_Springboot/tree/main/mvc2/thymeleaf-basic)

## 2. 타임리프 - 스프링 통합과 폼

## [3. 메시지, 국제화](https://github.com/beeguriri/Selfstudy_Springboot/tree/main/mvc2/message)
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

## [4. 검증 - Validation](https://github.com/beeguriri/Selfstudy_Springboot/tree/main/mvc2/validation)
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

## [5. 로그인 처리 - 쿠키, 세션, 필터, 인터셉터](https://github.com/beeguriri/Selfstudy_Springboot/tree/main/mvc2/login)
- 쿠키와 세션
  - 로그인에 성공하면 서버의 세션 저장소에 `JSESSIONID` 생성(추정불가능한 값)
  - HTTP 응답에 세션Id를 쿠키를 담아서 브라우저에 전달
  - 브라우저는 내부 쿠키 저장소에 쿠키 저장
  - 브라우저는 앞으로 해당쿠키를 지속해서 서버에 보내줌
```java
public String loginV3(..., HttpServletRequest request) {

    ...
    //로그인 성공 시
    //true: 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성 (default)
    //false : 세션이 있으면 있는 세션 반환, 없으면 null 반환
    HttpSession session = request.getSession(true);

    //세션에 로그인 회원 정보 보관
    session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

    return "redirect:/";
}
```
- 서블릿 필터와 스프링 인터셉터
  - HTTP 요청 -> WAS -> 필터 -> (디스패처)서블릿 -> 스프링 인터셉터 -> 컨트롤러
  - HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링인터셉터(적절하지 않은 요청 -> 컨트롤러 호출X)
```java
//WebConfig에 인터셉터 등록 후 사용
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      
      ...
      
      registry.addInterceptor(new LoginCheckInterceptor())
              .order(2)
              .addPathPatterns("/**") //모든 경로 허용
              .excludePathPatterns("/", "/members/add", "/login", "/logout",
                      "/css/**", "/*.ico", "/error"); //허용하지않을 경로
    }
}

//인터셉터
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER)==null) {
            log.info("미인증 사용자 요청");

            //로그인으로 redirect
            response.sendRedirect("/login?redirectURL="+requestURI);

            return false;
        }

        return true;
    }
}
```

## [6. 예외 처리와 오류 페이지](https://github.com/beeguriri/Selfstudy_Springboot/tree/main/mvc2/exception)
- API 예외처리 : 오류 응답 스펙을 정하고, JSON 으로 데이터를 내려줘야함
- ExceptionHandlerExceptionResolver
  - 컨트롤러에서 예외가 발생해도 서블릿 컨테이너까지 예외가 전달되지 않고
  - 스프링 MVC에서 예외처리가 끝이 남
  - ⭐`@ExceptionHandler`⭐
    - 특정 컨트롤러에서만 발생하는 예외를 별로도 처리
    - ExceptionResolver중 우선순위가 가장 높음
    - `@RestControllerAdvice`로 `@RestController`에서 분리해서 사용
```java
//Controller
//package나 annotation 기반 등으로 별도 지정해줄 수 있음
@RestControllerAdvice(basePackages = "study.exception.api")
public class ExControllerAdvice {
  ...

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) //응답코드 지정
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
      log.error("[exceptionHandler] ex", e);
      return new ErrorResult("BAD", e.getMessage());
    }
  ...
}
```

**8. 스프링 타입 컨버터**

**9. 파일 업로드**
