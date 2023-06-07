package beeguri.securitystudy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class MyFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    private String newName;
    private String originName;
    private String fileExtension;
    private long fileSize;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private MyFileList myFileList;

    //==생성메서드==//
    public static MyFile createMyFile(String newName, String originName, String fileExtension, long fileSize){

        MyFile myFile = new MyFile();

        myFile.setNewName(newName);
        myFile.setOriginName(originName);
        myFile.setFileExtension(fileExtension);
        myFile.setFileSize(fileSize);

        return myFile;
    }
}
