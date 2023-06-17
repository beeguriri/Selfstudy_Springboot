## 파일 업로드
- 파일은 바이너리 형태로 전송 됨
- 문자와 파일을 동시에 전송하기 위하여 `multipart/form-data` 방식 사용
  - form 태그에 enctype="multipart/form-data"라고 지정
- multipart 형식은 전송 데이터를 각 부분(Part)으로 나누어 전송

### 서블릿 파일업로드
- Part 주요 메서드
  - part.getSubmittedFileName() : 클라이언트가 전달한 파일명 읽기
  - part.getInputStream():  Part의 전송 데이터를 읽기
  - part.write(...):  Part를 통해 전송된 데이터를 저장

### 스프링 파일업로드
- ⭐`MultipartFile`⭐사용
- 파일은 서버나 특정 경로에 저장해놓고
- DB에는 그 경로만 저장함
- 파일 업로드
```java
@PostMapping("/upload") 
public String saveFile(@RequestParam String itemName, 
                            @RequestParam MultipartFile file) throws IOException {
    ...
    if (!file.isEmpty()) { 
        
        //파일이름 불러오기
        String fullPath = fileDir + file.getOriginalFilename();
        
        //파일 저장
        file.transferTo(new File(fullPath)); 
    }
    ...
```
- 이미지 보여주기
  - `Resource`
```java
@ResponseBody
@GetMapping("/images/{filename}")
public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
    //file: => 내부파일에 직접 접근
    return new UrlResource("file:" + fileStore.getFullPath(filename));
}
```
- 파일 다운로드
  - header에 contentDisposition 추가해서 return
```java
@GetMapping("/attach/{itemId}")
public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId){
    ...
    String encodeUploadFileName=UriUtils.encode(uploadFileName,StandardCharsets.UTF_8);
    String contentDisposition="attachment; fileName=\""+encodeUploadFileName+"\"";

    return ResponseEntity.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition)
              .body(resource);
}
```
 
