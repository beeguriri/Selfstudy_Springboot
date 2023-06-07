# 스프링 시큐리티
## 백엔드
- 디펜던시 추가
```java
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.h2database:h2'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```
- 작업중 ...
  - [x] 없는 id 입력 될 경우 예외 처리 : return id == null
  - [x] 비밀번호 잘못 될 경우 예외 처리 : return password == null
  - [ ] 회원 가입
  - [ ] 회원정보 수정
  - [ ] 회원 탈퇴

## 프론트엔드
- 설치
```bash
$ npm install react-router-dom
$ npm install axios
```
- 작업중 ...
  - [ ] 회원가입 로직 구현
  - [ ] 회원수정 폼

# 파일 업로드 테스트
### 리액트
- 인증된 사용자 접근페이지에서 파일 업로드 할수 있게 함
- 파일 (여러개) 업로드 위한 < input type="file" multiple >
- 파일을 스프링부트로 전송

### 스프링부트
- `MultipartFile[]`로 받아서 순회
- 업로드 요청기록(One)과 파일리스트(Many) 매핑
- 개별 파일의 이름은 중복방지를 위하여 새로운 파일명 만들기 (현재 날짜와 랜덤 정수값)
- `cascade = CascadeType.ALL`로 지정해서 요청기록 DB저장 시 개별 파일의 목록도 함께 저장
- 저장 후 요청기록의 Id값 반환해서
- 그 Id로 파일 목록 찾아서 플라스크로 보냄 `RestTemplate` 사용
- 요청정보에 url, List 전송
- 로컬 특정 폴더에 실제 파일 저장해놨으므로 플라스크에서 해당 경로에서 파일명으로 파일 찾아서 사용 예정

### 플라스크
- `from flask import Flask, request`로 data 받음
- 타입확인 : List
```python
print("json", type(request.get_json()['data']))
```
- 리턴결과 리액트에서 콘솔로 찍어보기 완료

### 남은일
- [ ] 파일목록으로 관련파일 open해서 전처리 후 json형태로 return
  - 스프링부트에서 `ResponseEntity`의 body에 return data 넣어서
  - 리액트로 전송하면
  - 리액트에서 전송 정보 처리
- [ ] 리액트에서 json파일을 필요한 형태로 가공해서 화면 나타냄 (지도 및 차트)


