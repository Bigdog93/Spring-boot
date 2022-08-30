package com.koreait.facebook_clone.user;

import com.koreait.facebook_clone.common.MyConst;
import com.koreait.facebook_clone.feed.model.FeedDTO;
import com.koreait.facebook_clone.feed.model.FeedDomain2;
import com.koreait.facebook_clone.security.model.CustomUserPrincipal;
import com.koreait.facebook_clone.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
    public void profile(Model model, UserEntity param, UserDTO param2, @AuthenticationPrincipal CustomUserPrincipal userDetails) { // 컨트롤러단에서 시큐리티에서 로그인 정보 얻어오는 방법
        if(param.getIuser() == 0) {
            param.setIuser(userDetails.getUser().getIuser());
        }
        param2.setToIuser(param.getIuser());

        model.addAttribute(myConst.PROFILE, service.selUserProfile(param2));
        model.addAttribute(myConst.PROFILE_LIST, service.selUserProfileImgs(param));
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

    @ResponseBody
    @GetMapping("/feedList")
    public List<FeedDomain2> selFeed2(FeedDTO param) {
        return service.selFeedList2(param);
    }

    @ResponseBody
    @PostMapping("/follow")
    public Map<String, Object> doFollow(@RequestBody UserFollowEntity param) {
        return service.insUserFollow(param);
    }

    @ResponseBody
    @DeleteMapping("/follow")
    public Map<String, Object> undoFollow(UserFollowEntity param) {
        return service.delUserFollow(param);
    }

    @ResponseBody
    @GetMapping("/getFollowList")
    public List<UserDomain> getFollowList(UserFollowEntity param) {
        return service.selUserFollowList(param);
    }

    @ResponseBody
    @GetMapping("/getFollowerList")
    public List<UserDomain> getFollowerList(UserFollowEntity param) {
        return service.selUserFollowerList(param);
    }
}
