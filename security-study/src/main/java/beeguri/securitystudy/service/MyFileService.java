package beeguri.securitystudy.service;

import beeguri.securitystudy.domain.Member;
import beeguri.securitystudy.domain.MyFile;
import beeguri.securitystudy.domain.MyFileList;
import beeguri.securitystudy.repository.MemberRepository;
import beeguri.securitystudy.repository.MyFileListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class MyFileService {

    private final MyFileListRepository myFileListRepository;
    private final MemberRepository memberRepository;

    public Long uploadFiles(String userid, MultipartFile[] multipartFiles) throws IOException {

        System.out.println("======service======");
        String UPLOAD_PATH = "D:\\temp";
        List<MyFile> list = new ArrayList<>();
        Member member = memberRepository.findByUserid(userid).get();

        for (MultipartFile file : multipartFiles) {

            String originName = file.getOriginalFilename(); // ex) 파일.jpg
            String fileExtension = originName.substring(originName.lastIndexOf(".") + 1); // 확장자
            originName = originName.substring(0, originName.lastIndexOf(".")); // 파일이름

            // 현재 날짜와 랜덤 정수값으로 새로운 파일명 만들기
            String newName = (new Date().getTime()) + "" + (new Random().ints(1000, 9999).findAny().getAsInt());
//            String fileId = UUID.randomUUID().toString();

            long fileSize = file.getSize(); // 파일 사이즈

            File fileSave = new File(UPLOAD_PATH, newName + "." + fileExtension); // ex) fileId.jpg
            if (!fileSave.exists()) { // 폴더가 없을 경우 폴더 만들기
                fileSave.mkdirs();
            }

            file.transferTo(fileSave); // fileSave의 형태로 파일 저장

            MyFile myFile = MyFile.createMyFile(newName, originName, fileExtension, fileSize);
            list.add(myFile);

        }

        MyFileList myFileList = MyFileList.createFileList(member, list);
        myFileListRepository.save(myFileList);

        return myFileList.getId();

    }

    public String test(Long id) {

        System.out.println("id: " + id);
        List<MyFile> fileList = myFileListRepository.findById(id).get().getFileItems();
        List<String> list = fileList.stream().map(MyFile::getNewName).toList();

        Map<String, List<String>> map= new HashMap<>();
        map.put("data", list);

        String flaskUrl = "http://localhost:5000/test";

        // RestTemplate 객체 생성
        // HTTP 통신을 위한 클래스. 이것을 WebClient로 바꿀 수 있다(비동기통신으로)
        RestTemplate restTemplate = new RestTemplate();

        // HTTP Headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, List<String>>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, request, String.class);

        return response.getBody();

    }
}
