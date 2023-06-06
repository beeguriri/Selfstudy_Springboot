# 스프링 MVC 2편 - 백엔드 웹 개발 활용 기술

## 프로젝트 생성 및 세팅
- 'https://start.spring.io/' 에서 프로젝트 생성
    - SpringBoot `2.7.12`
    - Gradle Groovy `7.6.1`
    - java `17`
    - Dependencies
        - WEB : `Spring Web`
            - TEMPLATE ENGINES : `Thymeleaf`
            - DEVELOPER TOOLS : `Lombok`
- IDE : IntelliJ
    - File > New > Project from Existing sources => `import`
    - Settings > Build,Execution,Deployment > Build Tools > Gradle > Build and Run => `IntelliJ IDEA`

## 빌드하기
```bash
# 빌드
$ .\gradlew build

# 실행
$ cd .\build\libs 
$ java -jar .\thymeleaf-basic-0.0.1-SNAPSHOT.jar
```
---

## 요구사항 분석 및 설계
