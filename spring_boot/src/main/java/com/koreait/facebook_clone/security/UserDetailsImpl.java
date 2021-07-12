package com.koreait.facebook_clone.security;

import com.koreait.facebook_clone.user.model.UserDomain;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class UserDetailsImpl implements UserDetails {
    // 시큐리티에 저장하기 전에 얘를 생성할 꺼.

    @Getter
    private UserDomain user;

    public UserDetailsImpl(UserDomain user) {
        this.user = user;
    } // 무조건 UserEntity 주소값을 이용해 객체 생성
    // 생성자를 통해 위에 UserDomain 타입에 생성.

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
}
