package com.koreait.facebook_clone.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder // 검색해서 알아볼 것. 얘를 객체화 할때 필요한 애들만 값 넣어서 생성 가능.(멤버필드별 생성자를 다 만들어준다고 생각하면 됨..?)
@NoArgsConstructor // 기본생성자 만들어주는 친구
@AllArgsConstructor // 빌더는 AllArgs 를 받는게 기본이라 이게 있어야 한다.
public class UserEntity {
    private int iuser;
    private String email;
    private String provider;
    private String pw;
    private String nm;
    private String tel;
    private String authCd;
    private String regdt;
    private String mainProfile;
}
