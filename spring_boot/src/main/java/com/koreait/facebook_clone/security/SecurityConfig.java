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
//스프링에 접근하기 전에 얘를 거친다. (필터영역)
public class SecurityConfig extends WebSecurityConfigurerAdapter {// 브라우저 킬때 한번 실행된다.
    // WebSecurityConfigurerAdapter 의 메소드를 오버라이딩 하여 쓰기 위해..
    // 그럼 스프링에서 @Configuration 어노테이션을 보고 SecurityConfig 의 오버라이딩 된 메소드로다가 실행해준다.(@EnableWebSecurity 를 줘야 시큐리티가 켜진다)

    @Autowired
    private UserDetailsService userDetails; // 세션처럼 브라우저 킬때마다.
    @Autowired private CustomOAuth2UserService customOauth2UserService;

    @Bean // 메소드에 빈 등록하면 리턴하는 값이 빈 등록이 된다. 외부(라이브러리 등)에서 만든 애들은 이렇게 빈 등록 할 수 있다.
    public PasswordEncoder passwordEncoder() { // PasswordEncoder(인터페이스) 타입의 객체를 리턴하는 메소드(리턴값이 빈 등록)
        return new BCryptPasswordEncoder(); // 스프링에 내장 되어 있는 BCrypt
    } // 시큐리티에게 너 암호화할때는 BCrypt 를 쓰라고 빈 세팅 해주는 것.

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/content/**", "/img/**", "/css/**", "/js/**", "/img/**", "/pic/**")
                .antMatchers("/favicon.ico", "/resources/**", "/error");
        // 주소값이 얘네로 시작하는 애들은 시큐리티가 무시한다.
        // favicon.ico 는 브라우저 title 옆에 아이콘이다.
    }

    @Override // 권한 처리
    protected void configure(HttpSecurity security) throws Exception {
        security.csrf().disable(); // csrf(요청 위조) 무력화

        security.authorizeRequests()
                .antMatchers("/user/login", "/user/join", "/user/auth").permitAll()// 얘네는 누구나 들어갈 수 있도록
                .anyRequest().authenticated(); // 위 외의 주소는 모두 인증을 받아야 한다.

        security.formLogin() // 로그인 했을때
                .loginPage("/user/login") // 이 페이지에서 날아온 form action="login" 인 친구를 캐치
                .usernameParameter("email") // 얘(위아래 두줄까지) 없으면 디폴트는 /login 이고 시큐리티에서 제공하는 로그인 창이 뜬다. name 값 디폴트도 username, password 다
                .passwordParameter("pw") // form 태그 안에 name(키값)을 설정
                .defaultSuccessUrl("/feed/home") // 로그인 성공하면 일로로
                .failureUrl("/user/login?error");

        security.oauth2Login()
                .loginPage("/user/login")
                .defaultSuccessUrl("/feed/home")
                .failureUrl("/user/login")
                .userInfoEndpoint() //OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당합니다.
                .userService(customOauth2UserService);

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
