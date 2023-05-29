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
