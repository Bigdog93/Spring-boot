package com.koreait.facebook_clone.user;

import com.koreait.facebook_clone.common.file.MyFileUtils;
import com.koreait.facebook_clone.common.mailsender.EmailServiceImpl;
import com.koreait.facebook_clone.common.auth.RandomCodeGenerator;
import com.koreait.facebook_clone.feed.FeedMapper;
import com.koreait.facebook_clone.feed.model.FeedDTO;
import com.koreait.facebook_clone.feed.model.FeedDomain2;
import com.koreait.facebook_clone.security.IAuthenticationFacade;
import com.koreait.facebook_clone.security.UserDetailsServiceImpl;
import com.koreait.facebook_clone.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired private UserMapper mapper;
    @Autowired private UserProfileMapper profileMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private RandomCodeGenerator securityUtils;
    @Autowired private EmailServiceImpl emailService;
    @Autowired // @Bean 이 있었기 때문에 가능
    private PasswordEncoder passwordEncoder;
    @Autowired private MyFileUtils fileUtils;
    @Autowired private IAuthenticationFacade auth;
    @Autowired private FeedMapper feedMapper;
    @Autowired private UserDetailsServiceImpl userDetailsService;

    public int join(UserEntity param) {
        String authCd = securityUtils.getRandomCode(5);

        // 비밀번호 암호화
        String hashedPw = passwordEncoder.encode(param.getPw());
        param.setPw(hashedPw);
        param.setAuthCd(authCd);
        param.setProvider("local");
        int result = userDetailsService.join(param);

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
        UserEntity loginUser = auth.getLoginUser();
        int iuser = loginUser.getIuser(); // 로그인한 사람의 pk 값
        String target = "profile/" + iuser; // 저장되는 경로

        UserProfileEntity profileEntity = new UserProfileEntity(); // 객체생성은 한번만 해도 된다.
        profileEntity.setIuser(iuser);

        for(MultipartFile img : imgArr) { // imgArr 의 length 가 0 이면 안돈다.
            String saveFileNm = fileUtils.transferTo(img, target); // 파일 업로드
            if(saveFileNm != null) {
                profileEntity.setImg(saveFileNm);
                int result = profileMapper.insUserProfile(profileEntity);

                if(loginUser.getMainProfile() == null && result == 1) {
                    UserEntity param = new UserEntity();
                    param.setIuser(loginUser.getIuser());
                    param.setMainProfile(saveFileNm);

                    if(userMapper.updUserProfile(param) == 1)
                        loginUser.setMainProfile(saveFileNm);
                }
            }
        }
    }

    public UserDomain selUserProfile(UserDTO param) {
        param.setFromIuser(auth.getLoginUserPk());
        return profileMapper.selUserProfile(param);
    }

    public List<UserProfileEntity> selUserProfileImgs(UserEntity param) {
        return profileMapper.selUserProfileList(param);
    }

    public Map<String, Object> updUserMainProfile(UserProfileEntity param) {
        UserEntity loginUser = auth.getLoginUser();

        param.setIuser(loginUser.getIuser());
        int result = mapper.updUserMainProfile(param);
        if(result == 1) { // 시큐리티 세션에 있는 loginUser 에 있는 mainProfile 값도 변경 해줘야 한다.
            loginUser.setMainProfile(param.getImg()); // 시큐리티에 있는 애 변경
        }
        Map<String, Object> res = new HashMap<>();
        res.put("result", result);
        res.put("img", param.getImg());
        return res;
    }

    public List<FeedDomain2> selFeedList2(FeedDTO param) {
        if(param.getIuserForMyFeed() == 0) {
            param.setIuserForMyFeed(auth.getLoginUserPk());
        }
        return feedMapper.selFeedList2(param);
    }

    public Map<String, Object> insUserFollow(UserFollowEntity param) {
        param.setIuserFrom(auth.getLoginUserPk());
        int test = mapper.insUserFollow(param);
        Map<String, Object> result = new HashMap<>();
        result.put("result", test);
        return result;
    }

    public Map<String, Object> delUserFollow(UserFollowEntity param) {
        param.setIuserFrom(auth.getLoginUserPk());
        int test = mapper.delUserFollow(param);
        Map<String, Object> result = new HashMap<>();
        result.put("result", test);
        if(test != 0) {
            UserFollowEntity param2 = mapper.selUserFollow(param);
            result.put("youFollowMe", param2);
        }
        return result;
    }

    public List<UserDomain> selUserFollowList(UserFollowEntity param) {
        param.setIuserFrom(auth.getLoginUserPk());
        return mapper.selUserFollowList(param);
    }

    public List<UserDomain> selUserFollowerList(UserFollowEntity param) {
        param.setIuserFrom(auth.getLoginUserPk());
        return mapper.selUserFollowerList(param);
    }
}
