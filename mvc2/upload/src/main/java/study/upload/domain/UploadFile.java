package study.upload.domain;

import lombok.Data;

@Data
public class UploadFile {
    
    private String uploadFileName; //클라이언트가 업로드 한 파일 이름
    private String storeFileName; //서버에서 관리 할 파일 이름

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
