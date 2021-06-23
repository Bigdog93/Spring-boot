package com.koreait.facebook_clone.user;

import com.koreait.facebook_clone.common.mailsender.EmailServiceImpl;
import com.koreait.facebook_clone.common.security.MySecurityUtils;
import com.koreait.facebook_clone.user.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
    @Autowired
    private UserMapper mapper;

    @Autowired
    private MySecurityUtils securityUtils;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired // @Bean이 있었기 때문에 가능
    private PasswordEncoder passwordEncoder;

    public int join(UserEntity param) {
        String authCd = securityUtils.getRandomCode(5);

        // 비밀번호 암호화
        String hashedPw = passwordEncoder.encode(param.getPw());
        param.setPw(hashedPw);
        param.setAuthCd(authCd);
        int result = mapper.join(param);

        if(result == 1) {
            String subject = "[얼굴책] 인증메일입니다.";
            String txt = String.format("<a href=\"http://localhost:8090/user/auth?email=%s&authCd=%s\">인증하기</a>", param.getEmail(), authCd);
            emailService.sendMimeMessage(param.getEmail(), subject, txt);
        }
        return result;
    }

    public int auth(UserEntity param) {
        return mapper.auth(param);
    }

    /*public String login(UserEntity param) { // 스프링 시큐리티로 할꺼니까 얘는 안씀
        UserDomain loginUser = mapper.selUser(param);
        if(loginUser != null) {
            if(BCrypt.checkpw(param.getPw(), loginUser.getPw())) {
                // 로그인 성공
            }
            else {
                // 비밀번호 틀림
            }
        }else {
            // 아이디 없음
        }
        return "";
    }*/

    public void profileImg(MultipartFile[] imgArr) {

    }
}
