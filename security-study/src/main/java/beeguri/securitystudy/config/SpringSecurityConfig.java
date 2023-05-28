package beeguri.securitystudy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터 체인에 등록
//secured annotation 활성화, PreAuthorize annotation 활성화 (해당 메서드만 인증필요 할때)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SpringSecurityConfig {

    @Bean
    public BCryptPasswordEncoder encoderPwd() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() //인증 필요
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") //인증 필요 && 권한필요
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") //인증 필요 && 권한필요
                .anyRequest().permitAll()
                .and()
                .formLogin()
//                .loginPage("/loginForm") // 인증이 필요한 페이지의 경우 로그인이 안되어있으면 해당 페이지로 넘어옴
                .loginProcessingUrl("/login") //login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줌
                .defaultSuccessUrl("/");
//                .and()
//                .oauth2Login()
//                .loginPage("/login")
//                .userInfoEndpoint()
//                .userService(principalOauth2UserService);

        return http.build();
    }


//    @Bean
//    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http
//            .csrf().disable()
////                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and()
//            .authorizeRequests()
//                    .antMatchers("/", "/api/login").permitAll()
//                    .anyRequest().authenticated()
//            .and()
//            .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("http://localhost:3000")
//                .invalidateHttpSession(true);
//
//        return http.build();
//    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers("/static/js/**","/static/css/**","/static/img/**","/static/frontend/**");
//    }

}
