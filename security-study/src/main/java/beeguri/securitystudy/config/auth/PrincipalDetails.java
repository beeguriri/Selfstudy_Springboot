package beeguri.securitystudy.config.auth;

import beeguri.securitystudy.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 시큐리티가 "/login" 주소 요청이 오면 낚아채서 로그인을 진행시킴
// 로그인 진행이 완료 되면 시큐리티 session을 만들어줌 (Security ContextHolder)
// 오브젝트타입 => Authentication 타입 객체
// Authentication 안에 User정보가 있어야 됨.
// User 오브젝트의 타입 => UserDetails타입 객체
// Security Session => Authentication => UserDetails
public class PrincipalDetails implements UserDetails {

    private final Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    // 해당 User의 권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRoles();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUserid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 휴면계정 설정 등
        return true;
    }
}
