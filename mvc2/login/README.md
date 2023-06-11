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

#### 💜 3. 필터

#### 💜 4. 인터셉터