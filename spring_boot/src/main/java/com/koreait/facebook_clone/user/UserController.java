package com.koreait.facebook_clone.user;

import com.koreait.facebook_clone.user.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/login")
    public void login() {}

    @GetMapping("/join")
    public void join(UserEntity userEntity) {}
    // th:object 에서 가져오는것. 원래는 @ModelAttribute("userEntity") 해줘야한다.
    // 이렇게 해주면 태그에 name 값을 알아서 준다. 필드명과 th:field 가 다르면 에러뜸 신뢰성 업

    @PostMapping("/join")
    public String joinProc(UserEntity userEntity) {
        service.join(userEntity);
        return "redirect:/feed/home";
    }
}
