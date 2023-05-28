## 1. 리액트에서 잘못된 로그인정보 전송했을때 스프링부트에서 처리하기
- 리액트에서 로그인정보(id, password) 전송 -> 스프링부트에서 id로 회원정보 찾음
- id와 password가 일치하면 response status 200
#### 문제
- 잘못된 id나 password 입력하면 response status 500 으로 리액트에서 추가 작업 불가
#### 해결
- 스프링부트 MemberService에서 
- id가 없으면 id null값 반환
- password가 틀리면 password null값 반환
- 리액트에서 null값에 따라 추가 액션 할 수 있게 함

## 2. 리액트에서 로그인 후 리다이렉트
- 리액트에서 로그인 성공하면 특정 페이지로 리다이렉트
#### 문제
- url로 접근 시 로그인 여부에 관계없이 접근 가능
