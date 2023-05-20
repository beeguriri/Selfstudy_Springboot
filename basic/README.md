# 스프링 핵심 원리 - 기본편
- 스프링없이 자바로 개발 후 스프링으로 변경

## 프로젝트 생성하기
- 'https://start.spring.io/' 에서 프로젝트 생성
  - SpringBoot `2.7.12`
  - Gradle Groovy `7.6.1`
  - java `17`
  - Dependencies
    - ...
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
