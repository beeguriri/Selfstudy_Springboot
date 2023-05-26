package beeguri.securitystudy.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Builder
@Getter
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userid;

    private String password;

    private String roles;

    private Member(Long id, String userid, String password, String roles) {
        this.id = id;
        this.userid = userid;
        this.password = password;
        this.roles = roles;
    }

    protected Member() {}

}
