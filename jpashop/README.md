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
+ IDE : IntelliJ
  + File > New > Project from Existing sources => `import`
  + Settings > Build,Execution,Deployment > Build Tools > Gradle > Build and Run => `IntelliJ IDEA`
+ DB : H2 `jdbc:h2:tcp://localhost/~/jpashop`

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
![](https://github.com/beeguriri/Selfstudy_Springboot/blob/Spring_jpa/images/4_entity.png)
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

