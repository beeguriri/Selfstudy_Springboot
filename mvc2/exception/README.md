# Exception

## 서블릿 예외처리
- Exception
  - **Exception 기본 상태코드 : `HTTP Status 500`**
- response.sendError(HTTP 상태 코드, 오류 메시지)
> 1. WAS(/error-ex, dispatchType=REQUEST) -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러  
> 2. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)  
> 3. WAS 오류 페이지 확인  
> 4. WAS(/error-page/500, dispatchType=ERROR) -> 필터(x) -> 서블릿 -> 인터셉터(x) -> 컨트롤러(/error-page/500) -> View
- 필터, 인터셉터가 두번 호출 되는 것을 방지하기 위해
  - 필터는 DispatchType 으로 중복 호출 제거 ( dispatchType=REQUEST : default)
  - 인터셉터는 경로 정보로 중복 호출 제거( excludePathPatterns("/error-page/**") )

## 스프링부트 예외처리
- 스프링부트가 ErrorPage 를 `/error` 라는 경로로 기본 오류 페이지를 설정함.
- `BasicErrorController` 라는 스프링 컨트롤러를 자동으로 등록 (`/error` 를 매핑해서 처리하는 컨트롤러)
- 에러 공통처리가 필요할 경우 `BasicErrorController` 상속받아서 기능 추가 구현

## API 예외처리
- 오류 응답 스펙을 정하고, JSON 으로 데이터를 내려줘야함
  - request header 에 `Accept`를 `application/json` 으로 해주면
  - `BasicErrorController`가 알아서 json 형태로 보내줌

### HandlerExceptionResolver
- 예외를 ExceptionResolver에서 모두 처리할 수 있음
  - 컨트롤러에서 예외가 발생해도 서블릿 컨테이너까지 예외가 전달되지 않고
  - 스프링 MVC에서 예외처리가 끝이 남
- 예외 상태코드를 변환 
  - 예외는 500 서버에러로 전달되므로
  - request 에러일때는 400으로 변환해서 서블릿으로 전달
- API 응답 처리
  - response.getWriter().println("hello")처럼 HTTP 응답바디에 직접 데이터 넣어줌
```java
//MyHandlerExceptionResolver.java : 내가 만든 HandlerException
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
  ...
  //예외를 먹고 서버에 400이라고 전달
  response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage()); 
  ...
}
```
```java
//WebConfig.java 에 내가 만든 HandlerException 등록
@Override
public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
  resolvers.add(new MyHandlerExceptionResolver());
}
```

### 스프링이 제공하는 ExceptionHandlerExceptionResolver
#### ⭐`@ExceptionHandler`⭐
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
#### `@ResponseStatus`, `ResponseStatusException`
- 예외에 따라서 HTTP 상태 코드를 변경
```java
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
public class BadRequestException extends RuntimeException{
}

//Controller
public class ApiExceptionController {
  ...

    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {
        throw new BadRequestException();
    }
  ...
}
```
```java
//Controller
public class ApiExceptionController {
  ...
  
    @GetMapping(value = "/api/response-status-ex2")
    public String responseStatusEx2() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }
  ...
}
```
#### DefaultHandlerExceptionResolver
- 스프링 내부에서 발생하는 예외 처리