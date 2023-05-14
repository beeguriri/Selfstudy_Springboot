## 프로젝트 생성하기
+ 'https://start.spring.io/' 에서 프로젝트 생성
  + SpringBoot `2.7.11`
  + Gradle Groovy `7.6.1`
  + java `17`
  + Dependencies
      + `Spring Web` `Thymeleaf`
+ IDE : IntelliJ
  + File > New > Project from Existing sources => `import`
  + Settings > Build,Execution,Deployment > Build Tools > Gradle > Build and Run => `IntelliJ IDEA`
+ 참고 문서
  + https://docs.spring.io/spring-boot/docs/2.7.11/reference/html/
  + https://www.thymeleaf.org/

## 뷰 만들기
+ Welcome page > resources/static/index.html
+ 컨트롤러에서 `viewName`을 리턴하면 뷰 리졸버(viewResolver)가 화면을 찾아서 처리
  + resources/templates/`viewName.html`

## 빌드하기
```shell
# 빌드
> .\gradlew
# 실행
> cd .\build\libs 
> java -jar .\hello-spring-0.0.1-SNAPSHOT.jar
```
