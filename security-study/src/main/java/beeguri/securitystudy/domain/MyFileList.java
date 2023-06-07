package beeguri.securitystudy.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class MyFileList {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_id")
    private Long id;

    private LocalDateTime requestDate;

    @OneToMany(mappedBy = "myFileList", cascade = CascadeType.ALL)
    private List<MyFile> fileItems = new ArrayList<>();

    // 양방향일때 연관관계 메서드 //
    public void addFileItem(MyFile myFile){
        fileItems.add(myFile);
        myFile.setMyFileList(this);
    }
//
    //==생성메서드==//
    public static MyFileList createFileList(List<MyFile> list){

        MyFileList myFileList = new MyFileList();

        myFileList.setRequestDate(LocalDateTime.now());

        for (MyFile myFile : list)
            myFileList.addFileItem(myFile);

        return myFileList;
    }

}
