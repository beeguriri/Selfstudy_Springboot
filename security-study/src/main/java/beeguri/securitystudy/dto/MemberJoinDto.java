package beeguri.securitystudy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class MemberJoinDto {

    private String userid;
    private String password;

}
