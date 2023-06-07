package beeguri.securitystudy.controller;

import beeguri.securitystudy.service.MyFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
public class MyFileController {

    private final MyFileService multipartService;

    @PostMapping("/api/uploadFiles")
    public ResponseEntity<Object> uploadFiles(MultipartFile[] multipartFiles) {

        Long fileListId;
        try{
            fileListId = multipartService.uploadFiles(multipartFiles);

        } catch (Exception e) {
            System.out.println("에러?");
            return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
        }

        System.out.println(ResponseEntity.ok().body(multipartService.test(fileListId)).toString());

        return ResponseEntity.ok().body(multipartService.test(fileListId));

    }
}
