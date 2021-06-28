package com.koreait.facebook_clone.security;

import com.koreait.facebook_clone.user.model.UserDomain;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.PrintStream;

@Component // 컴포넌트를 줬기때문에 빈 등록. 객체화가 됐음
public class AuthenticationFacadeImpl implements IAuthenticationFacade {

    @Override
    public UserDomain getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // getContext() : SecurityContextHolder 의 스태틱 메소드. getAuthentication() : 나의 Authentication 객체를 얻을 수있다.
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal(); // 원래 리턴은 Object 로. UserDetails 를 implements 한 애만 시큐리티에 저장할 수 있다.
        return userDetails.getUser();
    }

    @Override
    public int getLoginUserPk() {
        return getLoginUser().getIuser();
    }
}
