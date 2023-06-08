package beeguri.securitystudy.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String userid;

    private String password;

    private String roles;

    @OneToMany(mappedBy = "member")
    private List<MyFileList> reqList = new ArrayList<>();

    public Member(String userid, String password, String roles) {
        this.userid = userid;
        this.password = password;
        this.roles = roles;
    }

    protected Member() {}

}
