package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
//JPA 표준스펙에 entity 의 default 생성자 필요 (protected)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//연관관계필드는 toString 안하는게 좋음
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY) //x to one 은 반드시 지연로딩
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team!=null)
            changeTeam(team);
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    //연관관계 setting
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
