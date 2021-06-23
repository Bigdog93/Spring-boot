package com.koreait.facebook_clone.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 설정파일이라고 알려주는
@EnableWebSecurity // 시큐리티 적용
//@EnableGlobalMethodSecurity(securedEnabled = true) // 얘는 각 메소드에서 권한 검사 시행할 수 있게 해주는
//@RequiredArgsConstructor // 롬복꺼. Autowired 없이 final 로만 주소값 가져오는. (생성자로 가져온데)
public class SecurityConfig extends WebSecurityConfigurerAdapter {// 브라우저 킬때 한번 실행된다.

    @Autowired
    private UserDetailsService userDetails; // 세션처럼 브라우저 킬때마다.

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 스프링에 내장 되어 있는 BCrypt
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**").antMatchers("/favicon.ico", "/resources/**", "/error");
        // 주소값이 얘네로 시작하는 애들은 시큐리티가 무시한다.
        // favicon.ico 는 브라우저 title 옆에 아이콘이다.
    }

    @Override // 권한 처리
    protected void configure(HttpSecurity security) throws Exception {
        security.csrf().disable(); // csrf(요청 위조) 무력화

        security.authorizeRequests()
                .antMatchers("/user/login", "/user/join").permitAll()// 얘네는 누구나 들어갈 수 있도록
                .anyRequest().authenticated(); // 위 외의 주소는 모두 인증을 받아야 한다.

        security.formLogin() // 로그인 했을때
                .loginPage("/user/login") // 이 페이지에서 날아온 form action="login" 인 친구를 캐치
                .usernameParameter("email") // 얘(위아래 두줄까지) 없으면 디폴트는 /login 이고 시큐리티에서 제공하는 로그인 창이 뜬다.
                .passwordParameter("pw") // form 태그 안에 name(키값)을 설정
                .defaultSuccessUrl("/feed/home"); // 로그인 성공하면 일로로

        security.logout() // 로그아웃 했을 떄
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // 얘 주소로 들어왔을 때
                .logoutSuccessUrl("/user/login") // 성공하면 일로
                .invalidateHttpSession(true); // 로그인 정보 비우기

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder()); // 패스워드 인코딩 할때 쓰는 인코더 설정
    }
}
