package beeguri.securitystudy.controller;

import beeguri.securitystudy.dto.FileDto;
import beeguri.securitystudy.dto.MemberLoginDto;
import beeguri.securitystudy.service.MyFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MyFileController {

    private final MyFileService multipartService;

    @PostMapping("/api/uploadFiles")
//    public ResponseEntity<Object> uploadFiles(MultipartFile[] multipartFiles) {
    public ResponseEntity<Object> uploadFiles(
            @RequestPart(value = "userid") FileDto userid,
            @RequestPart(value = "multipartFiles") MultipartFile[] multipartFiles) {

        Long fileListId;

        try{
            System.out.println(userid.getUserid());
            System.out.println(multipartFiles.toString());
            fileListId = multipartService.uploadFiles(userid.getUserid(), multipartFiles);

        } catch (Exception e) {
            System.out.println("에러?");
            return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
        }

//        System.out.println(ResponseEntity.ok().body(multipartService.test(fileListId)).toString());

        return ResponseEntity.ok().body(multipartService.test(fileListId));
//        return null;

    }
}
