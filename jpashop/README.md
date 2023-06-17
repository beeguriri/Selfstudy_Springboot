# 실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발

## 프로젝트 생성 및 세팅
- 'https://start.spring.io/' 에서 프로젝트 생성
  - SpringBoot `2.7.12`
  - Gradle Groovy `7.6.1`
  - java `17`
  - Dependencies
    - WEB : `Spring Web`
      - TEMPLATE ENGINES : `Thymeleaf`
      - SQL : `Spring Data JPA`  `H2 Database` 
      - DEVELOPER TOOLS : `Lombok`  `Spring Boot DevTools`
      - External Library : `com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6`
      - I/O : `Validation`
- IDE : IntelliJ
  - File > New > Project from Existing sources => `import`
  - Settings > Build,Execution,Deployment > Build Tools > Gradle > Build and Run => `IntelliJ IDEA`
- DB : H2 `jdbc:h2:tcp://localhost/~/jpashop`

## 빌드하기
```bash
# 빌드
$ .\gradlew build

# 실행
$ cd .\build\libs 
$ java -jar .\jpashop-0.0.1-SNAPSHOT.jar
```
---

## 요구사항 분석 및 설계
![](https://github.com/beeguriri/Selfstudy_Springboot/blob/main/images/4_entity.png)

### 요구사항 분석
- 회원기능
  - 회원 등록, 회원 조회
- 상품 기능
  - 상품 등록, 상품 수정, 상품 조회 
  - 재고관리 필요
  - 상품의 종류는 도서, 음반, 영화
  - 상품을 카테고리로 구분
- 주문 기능
  - 상품주문, 주문 내역 조회, 주문 취소
  - 상품 주문시 배송 정보를 입력
### 테이블 설계
  - 회원은 `여러번 주문`할 수 있다. (1:`n`)
  - 한번 주문 시 `여러 주문 상품`을 선택할 수 있다. (1:`n`)
  - 하나의 주문 당 주소는 하나(1:1)
  - 하나의 아이템은 `여러 주문상품`이 될 수 있다. (1:`n`)
  - 상품은 카테고리를 가진다
  - 카테고리와 아이템은 `n`:`m` (실무에서는 @ManyToMany 사용X)
### 엔티티 클래스 개발
- value type
```java
  //주소 값 타입 (Embeddable)
  //생성할때만 값이 setting이 되고, 변경이 불가해야함(Immutable)
  @Embeddable //JPA의 내장타입
  @Getter
  public class Address {
    ...
  }
  
  @Embedded
  private Address address;
```
- 1:1, 1:n, n:m 매핑
  - 1:1의 관계에서는 access를 더 많이 하는 쪽에 `fk`
  - 1:n의 관계에서는 n 쪽에 `fk`
  - n:m의 관계에서는 `join table` 필요
    - 실무에서는 ManyToMany 쓰지말자
```java
  //1:1
  @OneToOne //fk
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

  @OneToOne(mappedBy = "delivery")
  private Order order;

  //1:n
  @ManyToOne //fk
  @JoinColumn(name="member_id")
  private Member member;

  @OneToMany(mappedBy = "member")
  private List<Order> orders = new ArrayList<>();
  
  //n:m
  @ManyToMany //n:m 은 조인테이블이 있어야 함.
  @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
  private List<Item> items = new ArrayList<>();

  @ManyToMany(mappedBy = "items")
  private List<Category> categories = new ArrayList<>();

```
### 엔티티 설계 시 주의점
- 실무에서는 Getter는 열어두되 Setter는 열어두지 말자
  - 변경 포인트가 너무 많아서 유지보수가 어려움
- 모든 연관관계는 `지연로딩(LAZY)`으로 설정
  - 즉시로딩(EAGER)은 예측이 어렵고, 어떤 SQL이 실행될 지 추적하기 어렵다. (n+1 문제)
  - 연관 된 엔티티를 함께 DB에서 조회해야하면, `fetch join` 또는 엔티티 그래프 기능을 사용
  - @OneToOne, @ManyToOne 관계는 기본이 즉시로딩이므로, 직접 지연로딩으로 설정해야 함
```java
@OneToOne(fetch = FetchType.LAZY)
@ManyToOne(fetch = FetchType.LAZY)
```
- Cascade
  - 연관관계의 경우 엔티티 당 persist 각각 호출해 주어야 하나
  - cascade => All 일 경우 order만 저장해주면 자동으로 orderItems 저장/삭제
- 양방향 연관관계 편의메서드

## 도메인 개발
### Test 설계
- `@Transactional` : 테스트 실행 후 롤백
  - 회원가입 메서드 검증 시, transaction commit 시점에 insert 쿼리가 나가므로
  - @Transactional 어노테이션이 있으면 insert 쿼리가 실행되지 않고 롤백
  - 쿼리 날라가는거 보기 위해 `flush` 해줌 
    ```java
    @Autowired EntityManager em;
    ...
    em.flush();
    ```
  - > o.s.t.c.transaction.TransactionContext   : Rolled back transaction for test
- DB에 저장되는거 보기 위해서는 `@Rollback(false)`
- 테스트 시 별도의 `application.yml` 만들어서 운영환경과 테스트 환경 분리해서 사용하자!
  > 별도 application.yml 파일 없을때 (분리전)  
  > connection 1| url jdbc:h2:tcp://localhost/~/jpashop
  
  > 메모리 DB 사용 시 (jdbc:h2:mem:test) (분리 후)
  > connection 1| url jdbc:h2:mem:test
  
  > 별도 설정 없을 경우 (분리 후)  
  > connection 2| url jdbc:h2:mem:bdc9394a...

### 도메인 모델 패턴
- 주문 서비스의 주문과 주문 취소 메서드를 보면 비즈니스 로직 대부분이 엔티티에 있다. 
- 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할을 한다.
- [참고사이트](https://martinfowler.com/eaaCatalog/domainModel.html)

### JPA에서 동적쿼리를 어떻게 해결하나?

## 웹 계층 개발
### Validation
- `@Valid` : MemberForm에 있는 Validation 사용
- `BindingResult` : 에러가 있으면 에러 저장 후 코드를 실행 함
  ```java
  @PostMapping(value = "/members/new")
  public String create(@Valid MemberForm form, BindingResult result){
  
      // error가 있으면 => form으로 다시 가겠다!
      if(result.hasErrors())
          return "members/createMemberForm";
      ...
  }
  ```

### 변경감지와 병합
- 준영속 엔티티
  - 새로운 객체를 생성했으나, 이미 DB에 한번 저장되어 식별자가 존재하는 엔티티
  - 기존에 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있음
  - JPA 가 관리를 하지않음
- **준영속 엔티티 수정방법**
  - 변경감지 기능 사용
    - 영속성 컨텍스트에서 엔티티 조회한 후 데이터 수정
    - 트랜잭션 안에서 엔티티 조회, 값 변경 ->
    - 트랜잭션 커밋 시점에 변경감지(Dirty Checking) 동작 ->
    - DB에 Update SQL 실행
> - 컨트롤러에서 엔티티를 생성하지 말자    
> - 트랜잭션이 있는 서비스 계층에 식별자와 변경할 데이터를 명확히 전달하자  
> - (엔티티를 파라미터로 쓰지말고 필요한 데이터만 사용)  
> - 업데이트 할 항목이 많으면 DTO 객체를 만들어서 사용하자  
> - 트랜잭션이 있는 서비스 내에서 엔티티를 조회하고 변경하자  

  - 병합(Merge) 사용
    - 준영속 엔티티의 식별자 값으로 영속 엔티티 조회
    - 병합을 사용하면 모든 속성이 변경 됨(병합시 값이 없으면 null 업데이트)
    - 사용 시 주의 필요


### 참고
- `Entity`는 핵심 비즈니스 로직만 가지고 있고, 화면을 위한 로직은 없어야 함
  - 화면에 맞는 폼객체나 DTO객체 만들어서 사용하는 것을 권장
  - API 만들때는 `Entity`를 절대 반환하면 안됨!!!

### THYMELEAF [참고문서](https://www.thymeleaf.org/doc/articles/layouts.html)
```html
 <!-- form 안에서 객체를 쓰겠다! -->
th:object="${memberForm}"

<!-- object를 참고하겠다! -->
th:field="*{name}"

<!-- th:field, th: value -->
th:field: 자동으로 타입컨버터 적용
th:value: *{name} : object.toString(), *{{name}} : 타입컨버터 적용

<!-- error 처리 -->
th:class="${#fields.hasErrors('name')}

<!-- for 순회 -->
th:each="member : ${members}"

<!-- null이면? 뒤에 진행 안해! -->
th:text="${member.address?.city}"

<!-- @ : url 명시 -->
th:action="@{/items/new}"
th:href="@{/items/{id}/edit (id=${item.id})}"
```
