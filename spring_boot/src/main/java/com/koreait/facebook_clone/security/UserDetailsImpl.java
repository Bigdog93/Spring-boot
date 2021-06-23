package com.koreait.facebook_clone.security;

import com.koreait.facebook_clone.user.model.UserDomain;
import com.koreait.facebook_clone.user.model.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UserDetailsImpl implements UserDetails {

    private UserEntity user;

    public UserDetailsImpl(UserDomain user) {
        this.user = user;
    }

    @Override // 권한 설정
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return null;
    }

    public String getNm() { return user.getNm(); }

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
