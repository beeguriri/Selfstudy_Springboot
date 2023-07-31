# 스프링 핵심 원리 - 고급편

## 프로젝트 생성 및 세팅
- 'https://start.spring.io/' 에서 프로젝트 생성
    - SpringBoot `2.7.13`
    - Gradle Groovy `7.6.1`
    - java `17`
    - Dependencies
        - WEB : `Spring Web`
        - DEVELOPER TOOLS : `Lombok`
- IDE : IntelliJ

## 빌드하기
```bash
# 빌드
$ .\gradlew build

# 실행
$ cd .\build\libs 
$ java -jar .\advanced-0.0.1-SNAPSHOT.jar
```
---

### 💜 로그 추적기
#### 요구사항 분석
- 모든 Public 메서드의 호출과 응답 정보를 로그로 출력
- 애플리케이션의 흐름을 변경하면 안됨.
  - 비즈니스 로직의 동작에 영향을 주면 안됨
- 메서드 호출에 걸린 시간
- 정상 흐름과 예외 흐름 구분 (예외 발생 시 예외 정보 남아야 함)
- 메서드 호출의 깊이 표현
- HTTP 요청 구분
  - HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분
  - 트랜잭션 ID (고객 요청이 시작해서 끝날 때 까지) (<=DB의 트랜잭션 아님)
```html
로그 시작: [796bccd9] OrderController.request()
로그 정상 종료: [796bccd9] OrderController.request() time=1016ms
로그 예외 종료:  [796bccd9] |   |<X-OrderRepository.save() time=0ms ex=java.lang.IllegalStateException: 예외 발생!
```
#### 로그추적기 v1. 프로토 타입 개발
- TraceId : 트랜잭션 ID, 깊이를 표현하는 클래스
- TraceStatus : 로그를 시작하면 시작과 끝이 있어야함
- 현재는 요청이 오면 TraceId 생성
- controller 에서 로그 찍고 service 로 넘어감
- service 에서 TraceId 유지하지않고 새로 생성, level==0으로 로그 찍고 Repository 로 넘어감
- repository 에서 TraceId 유지하지않고 새로 생성, level==0으로 로그 찍고 종료(종료할때는 statusId 유지)
- 문제
  - controller, service, repository 각각에서는 트랜잭션 ID 동기화 되어있음
  - HTTP 요청 단위로 트랜잭션 ID, 메서드 호출 깊이(level) 동기화 안됨

#### 로그추적기 v2. 파라미터로 동기화
- controller에서 트랜잭션 ID 생성
- controller -> service -> repository 에 트랜잭션 ID 넘겨줌
- service, repository에서는 beginSync() 메서드 호출
- 문제
  - traceId 동기화를 위하여 관련 메서드, 인터페이스가 있다면 인터페이스까지 모두 수정해야함.
  - begin, beginSync 각각 호출해줘야함
  - 만약 컨트롤러를 통해서 서비스를 호출하는 것이 아니라면 파라미터로 넘길 traceId가 없음.

#### 로그추적기 v3. 필드 동기화
- 필드를 통해서 traceId 동기화
- `FieldLogTrace` Config 파일에 `@Bean`으로 등록 : 싱글톤 
- 동시성 문제 발생
  - 1초 내에 2번이상 호출 시 트랜젝션ID 구분이 안됨
  - 쓰레드 번호는 다름 [nio-8080-exec-7] / [nio-8080-exec-8]
  - 스프링 빈처럼 `싱글톤 객체의 필드를 변경`하여 사용할 때 동시성문제 조심해야 함.
  - 참고 : 인스턴스 필드(주로 싱글톤) 또는 static 같은 공용 필드에 접근할 때 발생(읽기만 할때는 발생 안함)

#### 로그추적기 v3 개선. 쓰레드 로컬 `ThreadLocal<T>`
- 해당 쓰레드만 접근 할 수 있는 특별한 저장소
- set(), get(), remove() 메서드 사용
- 사용자A의 요청이 들어오면 WAS는 쓰레드풀에서 쓰레드를 하나 조회해서 할당
- `Thread-A`는 사용자A의 데이터를 쓰레드 로컬 전용보관소에 저장
- 사용자A의 응답이 끝나면 WAS는 `Thread-A`를 쓰레드풀에 반환
- 이 때 `Thread-A`의 쓰레드 로컬 전용보관소에 사용자A의 데이터가 살아있음
- 사용자B가 `Thread-A`를 할당받았을때 사용자A의 정보를 조회하게 됨.
- 사용자A의 요청이 끝나면 (쓰레드 로컬 완료 후) 저장된 값을 반드시 제거해주어야 함
- 문제 : 핵심 기능 코드에 try, catch 등 부가 기능(로그 관련) 코드가 붙어있어 복잡함 

#### 로그추적기 v4. 템플릿 메서드 패턴 (다형성)
- 핵심기능과 부가기능(핵심기능 보조)으로 나눔
- 변하는 것(핵심기능)과 변하지 않는것(부가기능)을 분리해서 모듈화
- 부모 클래스(추상클래스)에 변하지 않는 부분을 정의해두고
- 변하는 부분을 자식클래스(또는 익명내부클래스)에서 오버라이딩
- 단일책임의 원칙 지킬 수 있음
- 단점 (상속의 단점)
  - 자식클래스가 부모클래스를 상속받고 있으나 (강하게 의존) 부모클래스 메서드 사용 안함
  - 부모클래스가 변경이 되면 자식클래스도 변경이 되어야 함. (영향을 받음)
```mermaid
classDiagram
  class AbstractTemplate {
    <<abstract>>
    execute() //변하지 않는 부분
    call() //변하는 부분
  }
  class subClassLogic1 {
    call() //오버라이딩
  }
  class subClassLogic2 {
    call() //오버라이딩
  }
  AbstractTemplate <|-- subClassLogic1
  AbstractTemplate <|-- subClassLogic2
```
#### 로그추적기 v5. 템플릿 콜백 패턴 (전략패턴(위임) : 파라미터 방식)
- 변하지 않는 부분을 `Context`라는 곳에 두고,
- 변하는 부분을 `Strategy`라는 인터페이스를 만들고 구현
  - 인터페이스에 메서드가 한개만 있으면 람다로 더 간결하게 사용할 수 있음 
- 스프링에서 사용하는 의존관계주입과 같은 패턴
- 필드방식(선조립후실행) : context와 strategy 조립 후에는 변경이 번거로움
- 파라미터방식 : context의 메서드 실행할 때 strategy를 파라미터로 전달 => `템플릿 콜백 패턴`
```mermaid
classDiagram
class Context {
execute() //변하지 않는 부분
}
class Strategy {
  <<interface>>
call() //변하는 부분
}
class StrategyLogic1 {
call() //구현
}
class StrategyLogic2 {
  call() //구현
}
Context --> Strategy
StrategyLogic1 ..> Strategy
StrategyLogic2 ..> Strategy
```

#### 여기까지 로직 : v5 
- `ThreadLocalLogTrace` : `LogTrace` interface의 begin, end, exception 메서드 구현체
  - `ThreadLocal` 사용하여 traceId를 쓰레드로컬 전용보관소에 저장 
  - `@Configuration`에서 `@Bean`으로 등록해놓고 사용
- `TraceCallback`: 변하는 로직 인터페이스
- `TraceTemplate`에 변하지 않는 로직을 두고, 변하는 로직은 인터페이스 호출
  - `TraceTemplate` 생성 시 `LogTrace` 주입
  - `TraceTemplate`의 execute() 메서드 호출 시 `TraceCallback`인터페이스의 메서드 람다로 구현

### 💜 프록시패턴과 데코레이터 패턴
#### 프록시, 프록시 패턴, 데코레이터 패턴
```mermaid
classDiagram
class Subject {
  <<interface>> 
  operation()
}
class Proxy {
  operation() //구현
}
class RealSubject {
  operation() //구현
}
Client --> Subject
Proxy ..> Subject
RealSubject ..> Subject
```
- 클라이언트가 서버에 직접 요청 하는 것이 아니라
- 프록시를 통해 간접적으로 서버에 요청 함.
- 객체에서 프록시가 되려면, 서버와 프록시는 같은 인터페이스를 사용해야 함.
- 프록시의 주요 기능
  - `프록시패턴`: 접근제어 목적
    - 권한에 따른 접근 차단
    - 캐싱
    - 지연로딩
  - `데코레이터 패턴`: 부가 기능 추가 목적
    - 원래 서버가 제공하는 기능에 더해 부가 기능 수행 

#### 참고
- Application 하위 스캔대상 지정하기
```java
@Import({AppV1Config.class, AppV2Config.class})
@SpringBootApplication(scanBasePackages = "study.advanced.proxy")
public class AdvancedApplication {
  ...
}
```

#### 요구사항 분석
- **원본코드를 전혀 수정하지 않고** 로그 추적기 적용
- 특정 메서드는 로그를 출력하지 않는 기능

#### v1. 인터페이스와 구현 클래스 - 스프링 빈으로 수동 등록
#### v2. 인터페이스가 없는 구체 클래스 - 스프링 빈으로 수동 등록
#### v3. 컴포넌트 스캔으로 스프링빈 자동 등록
