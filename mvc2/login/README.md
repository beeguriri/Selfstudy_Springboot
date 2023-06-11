### 로그인

#### 💜 1. 쿠키
- 서버에서 로그인에 성공하면 HTTP 응답에 쿠키를 담아서 브라우저에 전달
- 브라우저는 내부 쿠키 저장소에 쿠키 저장
- 브라우저는 앞으로 해당쿠키를 지속해서 서버에 보내줌
- 세션 쿠키 : 브라우저 종료 시까지만 유지
```java
// 로그인에 성공하면 쿠키 발행
Cookie idCooke = new Cookie("memberId", String.valueOf(loginMember.getId()));
response.addCookie(idCooke);

// 로그아웃 하면 쿠키 삭제
Cookie cookie = new Cookie(cookieName, null);
cookie.setMaxAge(0);
response.addCookie(cookie);
```
- 쿠키의 값을 위변조가 가능해 보안상에 문제가 있음
  - 사용자 별로 예측할 수 없는 임의의 토큰 발행
  - 만료시간을 짧게 설정해 일정시간이 지나면 폐기

#### 💜 2. 세션 (서블릿 HTTP 세션)
- 로그인 하면 서버의 세션 저장소에 세션 Id(key) 생성 (추정불가능한 값)
- 세션Id를 응답쿠키로 전달
- 브라우저의 쿠키 저장소에 세션Id를 저장하고 있음
- 클라이언트와 서버는 결국 쿠키로 연결 되어야 함.
- 세션생성 / 조회 / 만료 기능
```java
//LoginController
@PostMapping("/login")
public String loginV3(..., HttpServletRequest request) {

    ...로그인 검증

    //true: 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성 (default)
    //false : 세션이 있으면 있는 세션 반환, 없으면 null 반환
    HttpSession session = request.getSession();

    //세션에 로그인 회원 정보 보관
    session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

    return "redirect:/";
}

@PostMapping("/logout")
public String logoutV3(HttpServletRequest request) {

    HttpSession session = request.getSession(false);
    
    if(session!=null)
        session.invalidate();

    return "redirect:/";
}

//LoginHomeController
@GetMapping("/")
public String homeLoginV3Spring(
        @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
    
    //세션에 회원 데이터가 없으면 home
    if (loginMember == null)
        return "home";
  
    //세션이 유지되면 로그인홈으로 이동
    model.addAttribute("member", loginMember);
  
    return "loginHome";
}
```

#### 💜 3. 서블릿 필터
- HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러
- HTTP 요청 -> WAS -> 필터(적절하지 않은 요청 -> 서블릿호출 X)
- 모든 고객의 요청 로그를 남기는 요구사항이 있다면 필터를 사용
```java
// WebConfig에 Filter 설정
@Bean
public FilterRegistrationBean logFilter() {

      FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
      
      filterRegistrationBean.setFilter(new LogFilter());
      filterRegistrationBean.setOrder(1);
      filterRegistrationBean.addUrlPatterns("/*");
      
      return filterRegistrationBean;
}

// LoginCheckFilter 구현
public class LoginCheckFilter implements Filter {

    //인증과 무관하게 항상 허용하는 리소스
    private static final String[] whiteList = {"/", "/members/add", "/login", "/logout", "/css/*"};
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String requestURI = httpRequest.getRequestURI();
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    
    try{
      log.info("인증 체크 필터 시작 {}", requestURI);
    
      if(isLoginCheckPath(requestURI)) {
        log.info("인증 체크 로직 실행 {}", requestURI);
        HttpSession session = httpRequest.getSession(false);
    
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
    
          log.info("미인증 사용자 요청 {}", requestURI);
    
          //로그인으로 redirect
          //로그인 성공하면 현재페이지로 돌아올 수 있게 redirect 정보 넣어줌
          httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
    
          return; //미인증 사용자는 다음으로 진행하지않고 종료!
        }
      }
      chain.doFilter(request, response);
    
    } catch (Exception e) {
      throw e;
    
    } finally {
      log.info("인증 체크 필터 종료 {}", requestURI);
    }
}
```

#### 💜 4. 스프링 인터셉터
- 스프링 MVC가 제공하는 기술
- HTTP 요청 -> WAS -> 필터 -> (디스패처)서블릿 -> 스프링 인터셉터 -> 컨트롤러
- HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링인터셉터(적절하지 않은 요청 -> 컨트롤러 호출X)
- 로그인 여부 체크하기 좋음
```java
//WebMvcConfigurer 구현, 인터셉터 등록
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

//LoginCheckInterceptor 구현
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
```