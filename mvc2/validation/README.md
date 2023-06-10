### 검증(Validation)
- 컨트롤러의 중요한 역할 중 하나는 HTTP 요청이 정상인지 검증하는 것!
- 클라이언트 검증은 조작할 수 있으므로 보안에 취약함
- 서버 만으로 검증하면 즉각적인 고객 사용성이 부족해짐

#### 💜 Version 1: Map 사용
- `${errors?.containsKey('price')}`
  - errors?.containsKey 에서 결과가 null이면 nullPointException 대신 null 반환함
    - null인 경우 : getMethod에서 처음 폼에 접근할 때
  - errors.containsKey 하면 .. nullPointException 발생
- 단점 : 타입 에러를 잡지는 못함 (400 에러 발생)

#### 💜 Version 2 : BindingResult.addError()
- 스프링이 제공하는 검증 오류를 보관하는 객체
- `순서중요` 항상 @modelAttribute 뒤에 BindingResult가 나와야 함
```java
public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, ...) {
    ...
}
```
- 바인딩 시 타입 에러가 나면
  - 오류 정보를 bindingResult에 담아서 컨트롤러를 호출
```java
//필드 에러 생성자
bindingResult.addError(new FieldError("modelAttribute의 object 이름", "필드명", "에러메시지"))

//에러 발생 시 사용자 입력 값을 유지하기 위한 생성자
//rejectedValue, bindingFailure, code, args 추가
bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
    
//글로벌 에러
bindingResult.addError("modelAttribute의 object 이름", "에러메시지")
```
- bindingFailure : 타입 에러 같은 바인딩 실패인지, 검증 실패인지
- `메시지 소스` 사용 할 때 codes, arguments 사용
```java
//errors.properties
required.item.itemName=상품 이름은 필수입니다. 
range.item.price=가격은 {0} ~ {1} 까지 허용합니다. 
    
//controller
//codes에 필요한 argument 없음
bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[] {"required.item.itemName"}, null, null));

//codes에 필요한 argument 있음
bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[] {"range.item.price"}, new Object[]{1000, 1000000}, null));
```
- thymeleaf 설정
```html
<!-- thymleaf에서 -->
<!-- 필드 에러 -->
<input type="text" id="itemName" th:field="*{itemName}" 
                   th:errorclass="field-error" class="form-control">
<div class="field-error" th:errors="*{itemName}">필드에러메시지</div>

<!-- 글로벌 에러 -->
<div th:if="${#fields.hasGlobalErrors()}">
	<p class="field-error" 
       th:each="err : ${#fields.globalErrors()}" th:text="${err}">전체 오류 메시지</p>
</div>
```

#### 💜 Version 3 : BindingResult.rejectValue() + DefaultMessageCodesResolver
```java
//필드명, 에러코드
bindingResult.rejectValue("itemName", "required");

//필드명, 에러코드, 에러 args, default message
bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);            
```
- `errors.properties` 에서 `에러 코드` 찾아서 처리
- DefaultMessageCodesResolver 기본 메시지 생성 규칙
  - 객체 에러
    - code.objectName
    - code
  - 필드 에러
    - code.objectName.field
    - code.field
    - code.fieldType
    - code
   

#### 💜 Version 4 : Validator 분리
- `@InitBinder` 로 validator 호출
- `@Validated` 어노테이션 사용
```java
@InitBinder
public void init(WebDataBinder dataBinder) {
   dataBinder.addValidators(itemValidator);
}
  
public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, ...) {
    ...
}
```
