package com.koreait.facebook_clone.security.model;

import com.koreait.facebook_clone.user.model.UserDomain;
import com.koreait.facebook_clone.user.model.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


public class CustomUserPrincipal implements UserDetails, OAuth2User {
    // 시큐리티에 저장하기 전에 얘를 생성할 꺼.

    @Getter
    private UserEntity user;
    private Map<String, Object> attributes;

    public CustomUserPrincipal(UserEntity user) {
        this.user = user;
    } // 무조건 UserEntity 주소값을 이용해 객체 생성
    // 생성자를 통해 위에 UserDomain 타입에 생성.

    // 얘는 auth2 로 로그인 할때 쓸 애.
    public CustomUserPrincipal(UserEntity user,Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override // 권한 설정
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return null;
    }

//    public String getNm() { return user.getNm(); }

//    public UserDomain getUser() {
//        return this.user;
//    }


    @Override
    public String getPassword() {
        return user.getPw();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override // 계정이 살아 있는지
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // 안 잠겼는가
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override // 계정이 활성화가 됐는가
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() { return attributes; }

    @Override
    public String getName() { return String.valueOf(user.getIuser()); }
}
