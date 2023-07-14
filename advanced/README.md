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

#### 로그추적기 v3. 쓰레드 로컬