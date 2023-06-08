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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 양방향일때 연관관계 메서드 //
    public void addFileItem(MyFile myFile){
        fileItems.add(myFile);
        myFile.setMyFileList(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getReqList().add(this);
    }

    //==생성메서드==//
    public static MyFileList createFileList(Member member, List<MyFile> list){

        MyFileList myFileList = new MyFileList();

        myFileList.setMember(member);
        myFileList.setRequestDate(LocalDateTime.now());

        for (MyFile myFile : list)
            myFileList.addFileItem(myFile);

        return myFileList;
    }

}
