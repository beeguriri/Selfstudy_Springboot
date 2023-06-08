//package beeguri.securitystudy;
//
//import beeguri.securitystudy.domain.MyFile;
//import beeguri.securitystudy.repository.MyFileListRepository;
//import beeguri.securitystudy.service.MyFileService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//public class MyFileServiceTest {
//
//    @Autowired
//    MyFileListRepository myFileListRepository;
//    @Autowired  MyFileService myFileService;
//
//    @Test
//    @Rollback(false)
//    public void 업로드테스트() throws Exception {
//        //given
//        MyFile myFile = MyFile.createMyFile("16861071920213370", "12345", "txt", 10);
//
//        //when
//        myFileListRepository.save(myFile);
//    }
//}
