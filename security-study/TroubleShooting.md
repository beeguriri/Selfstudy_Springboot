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


## 3. 파일 다중전송 하기
- 로그인성공 하면 접근할 수 있는 페이지에서 파일 여러개 첨부
- 첨부파일 전송 시 userid 함께 전송하려고 함
- 