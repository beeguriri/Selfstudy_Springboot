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

## API 개발 고급
