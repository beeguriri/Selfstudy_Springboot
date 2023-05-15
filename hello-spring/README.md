# 스프링 입문 - 코드로 배우는 스프링 부트, 웹 MVC, DB 접근 기술

## 프로젝트 생성하기
+ 'https://start.spring.io/' 에서 프로젝트 생성
  + SpringBoot `2.7.11`
  + Gradle Groovy `7.6.1`
  + java `17`
  + Dependencies
      + `Spring Web` `Thymeleaf`
      + `SpringData JPA`
+ IDE : IntelliJ
  + File > New > Project from Existing sources => `import`
  + Settings > Build,Execution,Deployment > Build Tools > Gradle > Build and Run => `IntelliJ IDEA`
+ DB : H2
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
> .\gradlew build
# 실행
> cd .\build\libs 
> java -jar .\hello-spring-0.0.1-SNAPSHOT.jar
```

> 참고: 스프링을 쓰는 이유? <br> 
> 개방폐쇄의 원칙 (Open-Closed Principle) : 확장에는 열려있고, 수정, 변경에는 닫혀있다. <br>
> 스프링의 DI를 사용하면, config(설정파일)만 수정하고 (다른 코드의 수정 없이) 인터페이스를 두고 구현체를 바꿔 끼울 수 있게 해줌 <br>

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
> 참고) Optional Class 사용하는 이유 : null값 처리할 수 있어서?!

<br> 

#### 🟪 스프링 빈 등록
> 참고: 스프링은 스프링 컨테이너에 스프링 빈을 등록할 때, `기본적으로 싱글톤`으로 등록한다.
(같은 스프링빈이면 모두 같은 인스턴스)
##### 1. 컴포넌트 스캔과 자동 의존관계 설정
+ 정형화 된 경우 사용
> 컴포넌트 스캔
+ `@SpringBootApplication` 클래스가 있는 패키지의 하위 클래스를 스캔해서 자동으로 등록해줌
+ `@Controller`, `@Service`, `@Repository` 각각 붙여서 사용
> 자동 의존관계 설정
+ `@AutoWired` : 스프링 컨테이너에 스프링 빈으로 등록되어있어야 사용할 수 있음

##### 2. 자바 코드로 직접 스프링 빈 등록
+ 정형화 되지 않거나, 상황에 따라 구현 클래스를 변경해야 하는 경우 사용
> @Configuration 어노테이션을 붙인 클래스를 만들고 @Bean으로 등록해서 사용
+ 수정이 필요 할 경우 Config 파일만 수정하면 됨 

<br>

#### 🟪 Test 설계
##### Java Code Test
+ 해당 클래스에서 `Ctrl+Shift+t` 누르면 자동으로 Test Class 만들 수 있음
+ 테스트 클래스의 메서드명은 보기쉽게 (보통) 한글로 작성하기도 함
+ 메서드 내 `given`, `when`, `then`  순으로 작성
```java
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
```
+ 테스트 실행되기 전에 생성자를 통해 인스턴스 생성
```java
@BeforeEach
public void beforeEach(){
    memberRepository = new MemoryMemberRepository();
    memberService = new MemberService(memberRepository);
}
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

##### Spring 통합 테스트
+ `@SpringBootTest` : 스프링 컨테이너와 테스트를 함께 실행
+ `@Transactional` : 테스트 시작전에 트랜잭션 시작, 테스트 완료 후 항상 롤백 (TestCase에서만)
+ Test할때는 필드주입 하는 이유는 케이스별로 주입해서 쓰고 끝이니까..! 

<br>

#### 🟪 의존성 주입 (Dependency Injection)
##### 생성자 주입
+ 어플리케이션 조립 되는 시점(생성자 호출시점)에 한번 호출 되는 것이 보장 됨
+ 의존관계가 실행중에 동적으로 변하는 경우가 거의 없으므로 생성자 주입 권장
+ 테스트 작성할 때 순수 자바 코드로 단위테스트 작성 가능
```java
//MemberController
@Controller
public class MemberController {

  private final MemberService memberService;

  @Autowired
  public MemberController(MemberService memberService) {

        this.memberService = memberService;
  }
}
```
##### 세터 주입
+ 주입받는 객체가 변경될 가능성이 있는 경우 사용(극히 드뭄)
+ 단점: public하게 노출이 되어 아무나 변경하게 될 수 있음

#####  ~~필드주입~~
+ ~~외부에서 접근이 불가능~~
+ ~~필드의 객체를 수정할 수 없음~~
+ ~~현재는 거의 사용하지 않는 방식~~

<br>

#### 🟪 JPA
> JDBC <br>
> JDBC Template <br>
> `JPA` => 스프링 데이터 JPA (라이브러리) <br>
+ 인터페이스를 통한 기본적인 CRUD 제공
+ 페이징 기능 자동 제공
+ 필요한 CRUD는 직접 구현 (-> 메서드 이름만 잘 써도...)

<br>

#### 🟪 AOP(Aspect Oriented Programming)
> 공통 관심 사항(cross-cutting concern) vs 핵심 관심 사항(core concern)
+ 핵심 관심사항과 공통 관심 사항을 분리하여 원하는 곳에 공통 관심 사항 적용
+ AOP를 적용하면 Controller가 Service를 호출할 때, 
+ 스프링컨테이너가 `프록시`를 통해 생성되는 가짜 Service 실행하고 `joinPoint.proceed()`를 통해 실제 Service를 실행함
```java
@Aspect
@Component //또는 Config 파일에 @Bean으로 등록
public class TimeTraceAop {
  ...
    @Around("execution(* study.hellospring..*(..))") //AOP가 적용 될 경로 지정
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        ...
        return joinPoint.proceed();
        ...
    }
}
```