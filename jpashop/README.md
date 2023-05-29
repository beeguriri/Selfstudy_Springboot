# 실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발

## 프로젝트 생성 및 세팅
+ 'https://start.spring.io/' 에서 프로젝트 생성
  + SpringBoot `2.7.12`
  + Gradle Groovy `7.6.1`
  + java `17`
  + Dependencies
      + WEB : `Spring Web`
      + TEMPLATE ENGINES : `Thymeleaf`
      + SQL : `Spring Data JPA`  `H2 Database` 
      + DEVELOPER TOOLS : `Lombok`  `Spring Boot DevTools`
      + External Library : `com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6`
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

