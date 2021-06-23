package com.koreait.facebook_clone.security;


import com.koreait.facebook_clone.user.UserMapper;
import com.koreait.facebook_clone.user.model.UserDomain;
import com.koreait.facebook_clone.user.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // UserDetails 를 뽑아서 시큐리티에게 주는 방법
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // 시큐리티에 저장할때 타입은 UserDetails
        UserEntity param = new UserEntity();
        param.setEmail(email);

        UserDomain loginUser = mapper.selUser(param);
        if(loginUser == null) {
            return null; // 아이디가 없는 상태
        }

        return new UserDetailsImpl(loginUser); // 아이디는 있는 상태
    }
}
