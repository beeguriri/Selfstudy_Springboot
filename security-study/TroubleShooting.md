## 1. 리액트에서 잘못된 로그인정보 전송했을때 스프링부트에서 처리하기
- 리액트에서 로그인정보(id, password) 전송 -> 스프링부트에서 id로 회원정보 찾음
- id와 password가 일치하면 response status 200
#### 문제
- 잘못된 id나 password 입력하면 response status 500 으로 리액트에서 추가 작업 불가
#### 해결1
- 스프링부트 MemberService에서 
- id가 없으면 id null값 반환
- password가 틀리면 password null값 반환
- 리액트에서 null값에 따라 추가 액션 할 수 있게 함
#### 해결2
- 스프링부트 MemberService에서 예외처리
- 리액트에서 catch문으로 잡음
```java
if(findMember.isPresent()) {
    if (bCryptPasswordEncoder.matches(params.getPassword(), findMember.get().getPassword()))
        return findMember.get();
    else throw new RuntimeException("비밀번호 오류");
} else throw new  RuntimeException("아이디 없음");
```
```javascript
...
.catch((error) => {
    console.log(error);
...
})
```

## 2. 리액트에서 로그인 후 리다이렉트
- 리액트에서 로그인 성공하면 특정 페이지로 리다이렉트 하려고 함
#### 문제
- url로 접근 시 로그인 여부에 관계없이 접근 가능
#### 해결
- 리액트에서 로그인에 성공하면 sessionStorage에 로그인여부 저장
- 특정 페이지로 리다이렉트 했을때, 
- sessionStorage 정보에 따라 원하는 페이지 띄우거나 로그인페이지로 리다이렉트 함


## 3. 파일 다중전송 하기 (리액트 -> 스프링)
- 로그인성공 하면 접근할 수 있는 페이지에서 파일 여러개 첨부
- 첨부파일 전송 시 userid 함께 전송하려고 함
- 스프링 : 전송요청과 유저정보 매핑, 전송요청 당 여러개 파일 매핑 해서 DB 저장
- 플라스크로 변환된 파일명 리스트 넘겨줌
#### 문제
- 파일은 json 형태로 전송할 수 없으므로 리액트에서 `multipart/form-data`형식으로 보냄
- userid 정보를 같이 보내고 싶은데 스프링에서 json parsing 못한다고 뜸  
#### 해결
- 헤더 정보에 "Content-Type": "multipart/form-data" 지정
- formData에 userid json형태로 append 및 각 파일 append
```javascript
formData.append('userid', 
    new Blob([JSON.stringify(sessionStorage.getItem('userid'))] , 
        {type: "application/json"}));

fileList.forEach((file) => {
    formData.append('multipartFiles', file);
});
```
- 스프링에서는 `@RequestPart`로 정보 받아 처리
```java
public ResponseEntity<Object> uploadFiles(
        @RequestPart(value = "userid") FileDto userid,
        @RequestPart(value = "multipartFiles") MultipartFile[] multipartFiles) {
        
    ...
}
```

### 4. 리액트 -> 스프링 -> 플라스크 한번에 연결
- 리액트에서 파일 전송 시
- 스프링에서 파일 정보 등 DB에 저장하고 요청한 파일들의 목록 1개의 ID를 반환
- 그 ID로 각 파일의 목록 뽑아서 그 리스트를 플라스크로 전송하고자 함
#### 문제
- 스프링 내에서 요청 처리한 ID 어떻게 기억하지?
- 바로 플라스크로 전송 할 방법 없나?
#### 해결
- `RestTemplate` 사용
- 요청, 응답 정보 json 형태로
```java
HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_JSON);

HttpEntity<Map<String, List<String>>> request = new HttpEntity<>(map, headers);

ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, request, String.class);
```
- 플라스크에서 json 데이터 받고 return
```python
request.get_json()

return "hello"
```
- 리액트 콘솔에 응답 정상적으로 오는 것 확인!!
