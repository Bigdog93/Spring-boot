package com.koreait.facebook_clone.security;

import com.koreait.facebook_clone.user.model.UserDomain;
import org.springframework.stereotype.Component;

public interface IAuthenticationFacade { // 인터페이스를 쓴다는건 유연하게 만드는 것. 안만들어도 됨 사실.
    UserDomain getLoginUser();
    int getLoginUserPk();
}
