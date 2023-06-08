[**1. 타임리프 - 기본 기능**](https://github.com/beeguriri/Selfstudy_Springboot/tree/main/mvc2/thymeleaf-basic)

**2. 타임리프 - 스프링 통합과 폼**

[**3. 메시지, 국제화**](https://github.com/beeguriri/Selfstudy_Springboot/tree/main/mvc2/message)
- 메시지 기능
  - HTML 파일에 하드코딩 된 메시지('상품명')를 
  - `messages.properties` 라는 파일을 만들어 한곳에서 관리
```html
 <label for="itemName">상품명</label>
```
- 스프링부트 기본 기능
```java
//application.properties
spring.messages.basename=messages

//messages.properties 
관리할 메시지 작성
```
- 타임리프에서 `th:text="#{page.items}"` 로 사용
  - 파라미터로 쓸 때는 `th:text="#{hello.name(${item.itemName})}"`
- 국제화
  - 메시지 파일을 각 나라 별로 별도 관리
  - `messages_en.properties` 등으로 파일 생성 해두면 됨

**4. 검증1 - Validation**

**5. 검증2 - Bean Validation**

**6. 로그인 처리1 - 쿠키, 세션**

**7. 로그인 처리2 - 필터, 인터셉터**

**8. 예외 처리와 오류 페이지**

**9. API 예외 처리**

**10. 스프링 타입 컨버터**

**11. 파일 업로드**
