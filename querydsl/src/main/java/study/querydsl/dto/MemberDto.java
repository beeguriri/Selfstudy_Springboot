package study.querydsl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data //getter, setter, requiredArgument,..
@NoArgsConstructor
public class MemberDto {

    private String username;
    private int age;

    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
