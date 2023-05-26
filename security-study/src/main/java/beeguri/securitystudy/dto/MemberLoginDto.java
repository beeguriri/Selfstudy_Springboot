package beeguri.securitystudy.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class MemberLoginDto {

    private String userid;
    private String password;

}
