package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //getter, setter, requiredArgument,..
@NoArgsConstructor
public class MemberDto {

    private String username;
    private int age;

    //Querydsl 에 의존성을 가짐
    @QueryProjection
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
