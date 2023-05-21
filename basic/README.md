# 스프링 핵심 원리 - 기본편
- 스프링없이 자바로 개발 후 스프링으로 변경

## 프로젝트 생성하기
- 'https://start.spring.io/' 에서 프로젝트 생성
  - SpringBoot `2.7.12`
  - Gradle Groovy `7.6.1`
  - java `17`
  - Dependencies
    - `lombok`
      - `File>Settings>Plugins`에서 `lombok` 설치
      - `File>Settings>Build, Execution, Deployment>Compiler>Annotation Processors`에서 `Enable` 체크
- IDE : IntelliJ
  - Settings > Build,Execution,Deployment > Build Tools > Gradle > Build and Run => `IntelliJ IDEA`
- DB : ...

## 테스트
- 해당 클래스에서 test 생성 : `Ctrl + Shift + t`
```java
import org.assertj.core.api.Assertions;
...
Assertions.assertThat(discount).isEqualTo(1000);
```

## 할인정책 적용
- 역할과 구현을 분리했으나 
  - DiscountPolicy Interface
  - FixDiscountPolicy, RateDiscountPolicy class
- `OrderServiceImpl`은 추상에도 의존하고 구체에도 의존함
  - DIP 위반
- 할인정책을 변경하려면 `OrderServiceImpl` 수정해야 하므로
  - OCP 위반
```java
// 기존 (OrderServiceImpl)
// private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
   private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
```
### 인터페이스에만 의존하도록 설계 변경 (관심사 분리)
```java
// 변경 (OrderServiceImpl)
// 추상화에만 의존하고, 변경에 닫혀있음
private DiscountPolicy discountPolicy;
```
- `AppConfig` : 애플리케이션 전체 동작 방식을 구성하기 위해 `구현객체를 생성`하고 `연결`하는 책임을 가지는 별도의 설정 클래스 만듦
- IoC 컨테이너 또는 `DI 컨테이너`라고도 함
- `구현객체를 생성`하고 
```java
public OrderService orderService() {
    return new OrderServiceImpl(new MemoryMemberRepository(),
                                    new FixDiscountPolicy());
}
```
- `연결` : 생성자를 통해 주입
```java
public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```
- 설계변경으로 `OrderServiceImpl`은 DiscountPolicy 인터페이스에만 의존한다
- `OrderServiceImpl` 의 생성자를 통해서 어떤 구현 객체을 주입할지는 오직 외부(`AppConfig`)에서 결정한다.
- `OrderServiceImpl` 은 이제부터 실행에만 집중하면 된다.
- `OrderServiceImpl` 에는 `MemoryMemberRepository`, `FixDiscountPolicy` 객체의 `의존관계가 주입`된다.


## 스프링 컨테이너와 스프링 빈
- `ApplicationContext`를 스프링 컨테이너라고 한다.
- `BeanFactory` : 스프링 컨테이너의 최상위 인터페이스
- 스프링컨테이너는 `@Configuration`이 붙은 `AppConfig`를 설정(구성) 정보로 사용한다.
- 여기서 `@Bean`이 적힌 메서드를 모두 호출해서 반환 된 객체를 스프링 컨테이너에 등록한다.
- 스프링빈은 `@Bean`이 붙은 메서드 명을 스프링 빈의 이름으로 사용한다. 빈 이름은 항상 다른 이름을 부여!!
- 사용할때는 applicationContext.getBean() 메서드를 사용해서 찾을 수 있다.

### 스프링 빈 조회하기
- 상속관계 : 부모타입으로 조회하면 모든 자식타입 조회 
  - Object 타입으로 조회하면, 모든 스프링 빈을 조회한다.

### ApplicationContext
- 빈 관리 기능
  - `BeanFactory` 상속받아서 제공
- 부가기능
  - `MessageSource` 활용한 국제화 기능
  - `EnvironmentCapable` 환경변수
  - `ApllicationEventPublisher` 애플리케이션 이벤트
  - `ResourceLoader` 편리한 리소스 조회

## 싱글톤 컨테이너
### 싱글톤 패턴
- 메모리 낭비를 줄이기 위하여 해당 객체는 1개만 생성되고 공유하도록 설계 한다
- 객체 인스턴스를 2개 이상 생성하지 못하도록
  - private 생성자를 사용해서 외부에서 임의로 `new` 키워드를 사용하지 못하도록 막아야 한다.
```java
// static 영역에 객체를 딱 1개만 생성
private static final SingletoneService instance = new SingletoneService();

// public으로 열어서 객체 인스턴스가 필요하면 이 static 메서드를 통해서만 조회하도록 함
public static SingletoneService getInstance() {
    return instance;
}

// 생성자를 private로 만들어 외부에서 new 키워드를 사용한 객체 생성을 못하게 막음
private SingletoneService() {
}
```
- 싱글톤 패턴의 문제점
  - 코드 자체가 많이 들어감 
  - 클라이언트가 구체 클래스에 의존한다 -> DIP위반
  - private 생성자로 자식 클래스 만들기 어려움
  - 결론적으로 유연성이 떨어진다

### 싱글톤 컨테이너
- 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤으로 관리
- 스프링 컨테이너가 빈 객체를 미리 생성해놓고 관리함

### 싱글톤 방식의 주의점
- 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에 상태를 유지(stateful)하게 설계하면 안됨
- 가급적 읽기만 가능해야함
- 스프링빈은 항상 무상태로 설계되어야 함
  - 필드 대신 자바에서 공유되지 않는 지역변수, 파라미터, ThreadLocal 등을 사용
```java
// 기존
public class StatefulService {

    private int price; // 상태를 유지하는 필드

    public void order(String name, int price) {
        this.price = price; // 여기가 문제!
    }
    
    public int getPrice() {
        return price;
    }
```
```java
// 변경
public class StatefulService {
    public int order(String name, int price) {
        return price;
    }
```

### `@Configuration`
- 스프링 컨테이너가 빈을 등록하고 관리
- Configuration <- AppConfig@CGLIB
- 빈이 등록되어 있으면 등록된 빈 반환, 아니면 새로운 빈 등록하고 반환
- 인스턴스 1개만 만들고 공유해서 사용하므로 싱글톤 보장
```text
AppConfig 호출 시 `MemoryMemberRepository` 세번 호출 할 것 같지만
@Bean MemberService -> new MemoryMemberRepository();
@Bean MemberRepository -> new MemoryMemberRepository();
@Bean OrderService -> new MemoryMemberRepository();

실제로는 한번만 호출하고 등록 된 빈 가져다 씀!
call AppConfig.memberService
call AppConfig.memberRepository
call AppConfig.orderService

memberService -> memberRepository1 = study.core.member.MemoryMemberRepository@7fcf2fc1
orderService -> memberRepository2 = study.core.member.MemoryMemberRepository@7fcf2fc1
memberRepository = study.core.member.MemoryMemberRepository@7fcf2fc1
```

## 컴포넌트 스캔
### 컴포넌트 스캔과 의존관계 자동 주입
- `@ComponentScan`은 `@component`가 붙은 모든 클래스를 스프링 빈으로 자동 등록 함
  - 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용
  - 기존에 `@Bean`으로 등록했던 것을 해당 클래스에서 `@component`등록해줌
- 기존에 `AppConfig`에서 의존관계 주입해 주었던 것을 해당 클래스에서 `@Autowired`로 자동 등록해줌
- `@ComponentScan` 대상
  - `@Component` : 컴포넌트 스캔에서 사용
  - `@Controlller` : 스프링 MVC 컨트롤러에서 사용
  - `@Service` : 스프링 비즈니스 로직에서 사용, 비즈니스 계측 인식에 도움 (특별한 처리는 하지 않음)
  - `@Repository` : 스프링 데이터 접근 계층에서 사용, **데이터 계층의 예외를 스프링 예외로 변환**
  - `@Configuration` : 스프링 설정 정보에서 사용, 스프링 빈이 싱글톤을 유지하도록 추가 처리

### 의존관계 주입
- **💜 생성자 주입 💜**
  - 생성자 호출 시점에 딱 1번만 호출되는 것이 보장된다.
  - `불변, 필수` 의존관계에 사용
  - 생성자가 1개만 있으면 `@Autowired` 생략 가능 (스프링 컨테이너에 자동으로 등록됨)
  - 필드에 `final` 키워드 사용할 수 있음 
    - 생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 단계에서 잡을 수 있음!
  - 최근에는 `lombok`의 `@RequiredArgsConstructor`를 이용하여 간결하게 만듦
    - `@RequiredArgsConstructor` : final이 붙은 필드로 생성자를 자동으로 만들어줌
- 수정자 주입(Setter 주입)
  - `선택, 변경` 가능성 있는 의존관계에 사용
- ~~필드 주입~~
  - 외부에서 변경이 불가능
  - 수정하려면 결국 setter가 필요함
  - 사용하지 말자! (테스트에서나 사용하자)

### 조회 빈이 2개 이상일 때
- `DiscountPolicy` 의 하위 타입인 `FixDiscountPolicy`, `RateDiscountPolicy` 둘다 스프링 빈으로 선언하면
- `UnsatisfiedDependencyException` 오류 발생
- 이때 하위 타입으로 지정하면 오류는 해결할 수 있지만 DIP를 위배하고 유연성이 떨어짐
- 해결1: `@Autowired 필드 명 매칭`
  - autowired는 기본적으로 타입으로 매칭함.
  - 여러 빈이 있으면 필드 이름, 파라미터 이름으로 빈을 추가 매칭
- 해결2: `@Qualifier`
  - 추가구분자
  - `@Qualifier("이름")` 의 이름 끼리 매칭
- 해결3: `@Primary`
  - 우선순위를 지정
  - 여러개의 빈이 있으면 `@Primary`가 붙은 빈을 매칭함

## 빈 생명주기
- 객체 생성 -> 의존 관계 주입
  - 예외 : 생성자주입은 객체 생성과 의존 관계 주입이 동시에 일어남
- 이벤트 라이프 사이클 (싱글톤)
  - 스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸전 콜백 -> 스프링 종료
- 객체의 생성과 초기화를 분리하자!

### 빈 생명주기 콜백
- ~~인터페이스(InitializingBean, DisposableBean)~~
  - 현재는 잘 사용하지 않는 방법
- 설정 정보에 초기화 메서드, 종료 메서드 지정
  - `@Bean(initMethod = "init", destroyMethod = "close")`
  - 메서드 이름을 자유롭게 줄 수 있다.
  - 스프링 빈이 스프링 코드에 의존하지 않음
  - 코드가 아니라 설정 정보를 사용하기 때문에, 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있음
- **`@PostConstruct`, `@PreDestroy` 어노테이션 지원**
  - 자바표준 `JSR-250`
  - 스프링이 아닌 다른 컨테이너에서도 동작
  - 컴포넌트 스캔과 잘 어울림
  - 단점 : 외부 라이브러리에는 적용하지 못함 (외부라이브러리 초기화, 종료 할때는 @Bean 기능 사용하자)