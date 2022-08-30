package com.koreait.facebook_clone.security;


import com.koreait.facebook_clone.security.model.CustomUserPrincipal;
import com.koreait.facebook_clone.user.UserMapper;
import com.koreait.facebook_clone.user.model.UserDomain;
import com.koreait.facebook_clone.user.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // UserDetails 를 뽑아서 시큐리티에게 주는 방법(아이)
public class UserDetailsServiceImpl implements UserDetailsService { // implements 를 해줘야 form login 했을때 이쪽으로 정보를 준다.(username 정보)

    @Autowired
    private UserMapper mapper;

//    @Override // UserDetails 타입으로 객체를 만들어서 리턴(시큐리티에 저장)
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // 시큐리티에 저장할때 타입은 UserDetails
//        UserEntity param = new UserEntity();
//        param.setEmail(email);
//
//        UserDomain loginUser = mapper.selUser(param);
//        if(loginUser == null) {
//            return null; // 아이디가 없는 상태
//        }
//
//        return new CustomUserPrincipal(loginUser); // UserDetails 에 유저 정보를 담아서 시큐리티에 리턴
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new CustomUserPrincipal(loadUserByUsernameAndProvider(email, "local"));
    }

    public UserDomain loadUserByUsernameAndProvider(String id, String provider) throws UsernameNotFoundException {
        UserEntity param = new UserEntity();
        param.setProvider(provider);
        param.setEmail(id);
        UserDomain user = mapper.selUser(param);
        if(user == null) {
            user = new UserDomain();
        }
        return user;
    }

    public int join(UserEntity param) { // UserService 에서는 하는일이 많기 때문에 mapper 넣는 기능만 따로 빼서 이쪽으로.
        if(param == null) { return 0; }
        return mapper.join(param);
    }
}
// 그러면 시큐리티에서 UserDetailsImpl 의 getPassword 메소드로 패스워드를 불러와서 Config 에서 설정해준 암호화 프로세스(비크립트)를 이용해 비교한다.
// 그러고 비밀번호가 일치하면 시큐리티 세션에 UserDeatils 저장