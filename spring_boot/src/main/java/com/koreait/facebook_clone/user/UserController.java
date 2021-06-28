package com.koreait.facebook_clone.user;

import com.koreait.facebook_clone.common.MyConst;
import com.koreait.facebook_clone.security.UserDetailsImpl;
import com.koreait.facebook_clone.user.model.UserDomain;
import com.koreait.facebook_clone.user.model.UserEntity;
import com.koreait.facebook_clone.user.model.UserProfileEntity;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private MyConst myConst;

    @GetMapping("/login")
    public void login(UserEntity userEntity) {}

   /* @PostMapping("/login")
    public String loginProc(UserEntity param) {
        return service.login(param);
    }*/

    @GetMapping("/join")
    public void join(UserEntity userEntity) {}
    // th:object 에서 가져오는것. 원래는 @ModelAttribute("userEntity") 해줘야한다.
    // 이렇게 해주면 태그에 name 값을 알아서 준다. 필드명과 th:field 가 다르면 에러뜸 신뢰성 업

    @PostMapping("/join")
    public String joinProc(UserEntity userEntity) {
        service.join(userEntity);
        return "redirect:/user/login?needEmail=1";
    }

    @GetMapping("/auth")
    public String auth(UserEntity param) {
        int result = service.auth(param);
        return "redirect:login?auth=" + result;
    }

    @GetMapping("/profile")
    public void profile(Model model, UserEntity param, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserEntity loginUser = userDetails.getUser();
        model.addAttribute(myConst.PROFILE_LIST, service.selUserProfileImgs(loginUser));
    }

    @PostMapping("/profileImg")
    public String profileImg(MultipartFile[] imgArr) { // 폼 태그안 인풋 type=file 안에 있던 애들. name 과 변수명을 맞춰줘야 한다.
        service.profileImg(imgArr);
        return "redirect:/user/profile";
    }

    @ResponseBody
    @GetMapping("/mainProfile")
    public Map<String, Object> mainProfile(UserProfileEntity param) {
        return service.updUserMainProfile(param);
    }

}
