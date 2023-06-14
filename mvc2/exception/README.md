### Exception

#### 서블릿 예외처리
- Exception
  - Exception 기본 상태코드 : `HTTP Status 500`
- response.sendError(HTTP 상태 코드, 오류 메시지)
> 1. WAS(/error-ex, dispatchType=REQUEST) -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러  
> 2. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)  
> 3. WAS 오류 페이지 확인  
> 4. WAS(/error-page/500, dispatchType=ERROR) -> 필터(x) -> 서블릿 -> 인터셉터(x) -> 컨트롤러(/error-page/500) -> View
- 필터, 인터셉터가 두번 호출 되는 것을 방지하기 위해
  - 필터는 DispatchType 으로 중복 호출 제거 ( dispatchType=REQUEST : default)
  - 인터셉터는 경로 정보로 중복 호출 제거( excludePathPatterns("/error-page/**") )

#### 스프링부트 예외처리
- 스프링부트가 ErrorPage 를 `/error` 라는 경로로 기본 오류 페이지를 설정함.
- `BasicErrorController` 라는 스프링 컨트롤러를 자동으로 등록 (`/error` 를 매핑해서 처리하는 컨트롤러)
- 에러 공통처리가 필요할 경우 `BasicErrorController` 상속받아서 기능 추가 구현