# 실전! 스프링 부트와 JPA 활용2 - API 개발과 성능 최적화

## 프로젝트 생성 및 세팅
- JPA1 강의에서 사용했던 코드 수정

## API 개발 기본
### ⭐Entity와 API 분리⭐
#### API를 개발할때는 Entity를 Parameter로 받지 말자!
  - Entity와 API가 1:1로 매핑되어있으면 Entity가 변경되었을 경우 API Spec이 변경되는 문제가 발생
  - API 요청 스펙에 맞추어 `별도의 dto`로 받는 게 좋음
#### Entity를 바로 반환하지 말자!
  - 응답값으로 엔티티를 직접 외부에 노출 하면 엔티티의 모든 값이 노출 됨 
  - json 확장성도 좋지 않음!
    - Result 라는 클래스를 만들어서 Dto를 한번 더 감싸줌
> Failure while trying to resolve exception [org.springframework.http.converter.HttpMessageNotWritableException]
  - 연관관계 매핑이 되어있을 경우, 회원정보 조회 시 에러 발생
    - Entity에 `@JsonIgnore` 붙여주면 해결 가능하나, 
    - 다른 API에서는 필요한 기능일 수 있음..
    - Entity에 Presentation 계층을 위한 로직이 추가되면 안됨! (양방향 의존관계)  

## 지연 로딩과 조회 성능 최적화
- ManyToOne, OneToOne 조회 시

### 지연로딩(fetch = FetchType.LAZY)
- X to One 관계는 기본이 EAGER임 -> LAZY로 변경 
- (연관 관계에 있는 Entity 무조건 가져오지 않고, getter 로 접근할 때 가져옴)
- 양방향 연관관계 무한루프 방지 위해 한쪽 Entity에 `@JsonIgnore` 붙여주고,
- 원하는 정보 Lazy 강제 초기화
- Entity를 DTO로 변환해서 반환하면 `1+N` (최대 : 1 + N + N) 발생

### ⭐Fetch Join⭐
- Entity를 DTO로 변환
- `Fetch Join`을 통해 성능을 최적화
```java
//OrderRepositoy
public List<Order> findAllWithMemberDelivery() {
    return em.createQuery(
                    "select  o from Order o " +
                    "join fetch o.member m " +
                    "join fetch o.delivery d", Order.class)
                .getResultList();
}
```
```java
//Controller
@GetMapping("/api/v3/simple-orders")
public List<SimpleOrderDto> orderV3() {

    //엔티티로 조회
    List<Order> orders = orderRepository.findAllWithMemberDelivery();

    //Dto로 반환
    return orders.stream()
                    .map(o -> new SimpleOrderDto(o))
                    .collect(Collectors.toList());
}
```

## 컬렉션 조회 최적화
- OneToMany 조회 시
- One을 기준으로 페이징을 하는 것이 목적이나, 
- many 를 기준으로 row 가 생성됨. (정규화X, 데이터 중복 발생)

### ⭐default_batch_fetch_size⭐
- x To One 은 `fetch join`을 해서 쿼리수를 줄이고, 
  - `x To One`은 paging 에 영향을 주지 않음
  - One To Many를 `fetch join` 하면 쿼리수는 줄어드나, 페이징 불가능
- 컬렉션 지연로딩 최적화를 위하며  
- `default_batch_fetch_size=100` 설정
- > select ... where orderitems0_.order_id in (?, ?)
    - => In 쿼리를 이용해서 쿼리 중복 줄여줌 (1+N -> 1+1)
  - size에 따라 쿼리수가 한번에 나감
    - 지연로딩이 발생하는 객체를 버퍼처럼 size개수만큼 모아놨다가, 
    - default_batch_fetch_size로 지정한 개수가 다 차게 되면 쿼리가 발생함. 
    - 예를들어 5로 setting 하면 in(1,2,3,4,5) 한번, in(6,7,8,9,10) 이런식으로 나감..


## OSIV와 성능 최적화
