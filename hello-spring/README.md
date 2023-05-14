## 프로젝트 생성하기
+ 'https://start.spring.io/' 에서 프로젝트 생성
  + SpringBoot `2.7.11`
  + Gradle Groovy `7.6.1`
  + java `17`
  + Dependencies
      + `Spring Web` `Thymeleaf`
+ IDE : IntelliJ
  + File > New > Project from Existing sources => `import`
  + Settings > Build,Execution,Deployment > Build Tools > Gradle > Build and Run => `IntelliJ IDEA`
+ 참고 문서
  + https://docs.spring.io/spring-boot/docs/2.7.11/reference/html/
  + https://www.thymeleaf.org/

## 뷰 만들기
+ Welcome page > resources/static/index.html
+ 컨트롤러에서 `viewName`을 리턴하면 뷰 리졸버(viewResolver)가 화면을 찾아서 처리
  + resources/templates/`viewName.html`

## 빌드하기
```shell
# 빌드
> .\gradlew
# 실행
> cd .\build\libs 
> java -jar .\hello-spring-0.0.1-SNAPSHOT.jar
```

---

### 스프링 웹 개발
#### 🟪 정적 컨텐츠 방식
> /resources/static/xxx.html
+ 별다른 변환 없이 html 파일 냅다 넘겨줌

<br>

#### 🟪 MVC와 템플릿 엔진 방식
> /resources/templates/xxx.html
+ Model, Controller : 비지니스 로직
+ View : 화면에 관련된 것만 분리해서 개발
+ 뷰리졸버가 템플릿 파일 매핑
+ 템플릿엔진이 HTML 로 변환 후 웹브라우저에 넘겨줌

<br>

#### 🟪 API 방식
> @ResponseBody
+ http 바디에 데이터 반환(객체 반환하면 => default 가 json)
  + httpMessageConverter 가 동작
  + 기본문자 : StringHttpMessageConverter
  + 기본객체 : MappingJackson2HttpMessageConverter
+ 서버 간 통신

---

### 회원관리 예제
+ Controller : 웹MVC의 컨트롤러 역할
+ Service : 핵심 비즈니스 로직 구현
+ Repository : 데이터베이스 접근, 도메인 객체를 DB에 저장하고 관리
+ Domain : 비즈니스 도메인 객체, 주로 데이터베이스에 저장하고 관리 됨
> 참고) Optional Class 사용하는 이유 : <br>
> null값 처리할 수 있어서?!

<br> 

#### 🟪 Test 설계
+ 해당 클래스에서 `Ctrl+Shift+t` 누르면 자동으로 Test Class 만들 수 있음
+ 테스트 클래스의 메서드명은 보기쉽게 (보통) 한글로 작성하기도 함
+ 메서드 내 `given`, `when`, `then`  순으로 작성
```java
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
```
+ 각 메서드는 서로 의존관계가 없이 설계되어야 한다
+ 하나의 테스트가 끝날때 마다 공용데이터나 저장소를 지워줘야함
```java
@AfterEach
public void afterEach() {
    repository.clearStore();
}
```
+ Test는 정상케이스 확인도 중요하지만 예외처리 확인도 반드시 해야함!
```java
//예외 처리 확인
IllegalStateException e = assertThrows(IllegalStateException.class,
                            () -> memberService.join(member2));
//예외 메세지 확인
assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
```
<br>

#### 🟪 의존성 주입 (Dependency Injection)
+ MemberService 호출 시 외부에서 의존성 주입할 수 있도록 생성자 만들어서
+ MemberServiceTest에서의 memberRepository instance와 MemberService에서의 memberRepository instance 동일하게 만듦
> MemberService
```java
public MemberService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
}
```
> MemberServiceTest
```java
@BeforeEach
public void beforeEach(){
    memberRepository = new MemoryMemberRepository();
    memberService = new MemberService(memberRepository);
}
```
