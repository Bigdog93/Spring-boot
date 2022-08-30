package com.koreait.facebook_clone.security;

import com.koreait.facebook_clone.security.model.*;
import com.koreait.facebook_clone.user.model.UserDomain;
import com.koreait.facebook_clone.user.model.UserEntity;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    // Default 에서 수정 필요.. 수정하고 나서 SecurityConfig 에서 얘 언급해서 설정 바꿔줌

    @Autowired
    private UserDetailsServiceImpl myUserService;

    @SneakyThrows
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        // 소셜로그인 성공하면 소셜로부터 이 request 에 정보들이 담겨서 날아온다.
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        // 여기서 oAuth2User 가 디폴트가 리턴하는 애임
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        // 카카오로 했는지 네이버로 했는지. yaml 로부터 registrationId 아래의 google: 등의 값이 넘어온다.

        Map<String, Object> attributes = oAuth2User.getAttributes();
        // 넘어온 애들이 담겨있는 곳. JSON 형태로 넘어오지만 HashMap 으로 바꿔준다. 또한 수정불가(무터블)이다.
        Map<String, Object> modifyAttributes = modifyUserAttributes(registrationId, attributes);
        // 국내 소셜은 구조가 다르기 때문에 얘가 필요.. 페북, 구글 같은것만 썼으면 안써도 됨.
        // 구글, 페북은 1차로 정보가 다 넘어오는데 네이버는 response 라는 객체 안에 값들이 들어있다.

        OAuth2UserInfo userInfo = getOauth2UserInfo(registrationId, modifyAttributes);
        // 얘가 하는 일은 통일 시키는 일(메소드명이나 이런것들. 하나의 클래스로 묶어주는 것)
        // 소셜마다 attribute 의 이름이 다르기 때문에 각각의 소셜마다 OAuth2UserInfo 를 상속받은 클래스를 만들어서
        // 하나의 메소드로 모든 소셜 정보들을 get 할 수 있도록.
        UserEntity user = convertOauthToUserEntity(userInfo);
        // 데이터베이스에 저장하기 위한 컨버터(UserEntity 로 DB 에 넣어야 하니까)
        UserEntity chkUser = myUserService.loadUserByUsernameAndProvider(user.getEmail(), user.getProvider());
        //DB에 값 있나 체크 없으면 insert
        if(chkUser == null) {
            myUserService.join(user);
            chkUser = user;
        }
        CustomUserPrincipal loginUser = new CustomUserPrincipal(chkUser, attributes);
        return loginUser;
    }
    private UserEntity convertOauthToUserEntity(OAuth2UserInfo userInfo) {
        UserEntity user = UserEntity.builder()
                // 빌더패턴을 여기서 쓴다. Setter 없이 값 넣고 싶을때 쓰는 애(?) 생성하면서 얘네 넣어줄 수 있다.(따로 생성자 없이)
                // return this; 로 자기자신을 반환, 다음에 또 자기자신의 메소드 호출 가능(체이닝)
                .email(userInfo.getId()) //social 사이트의 pk값을 우리 user테이블에 email에 저장함.
                .nm(userInfo.getName())
                .provider(userInfo.getProvider())
                .build();
        return user;
    }

    private OAuth2UserInfo getOauth2UserInfo(String registrationId, Map<String, Object> attributes) throws OAuth2AuthenticationProcessingException {
        if(registrationId.equalsIgnoreCase(SocialType.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(SocialType.FACEBOOK.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(SocialType.KAKAO.toString())) {
            return new KakaoOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(SocialType.NAVER.toString())) {
            return new NaverOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }

    private Map<String, Object> modifyUserAttributes(String registrationId, Map<String, Object> attributes) {
        Map<String, Object> mod = new HashMap(attributes); // 그대로 어트리뷰트를 새로운 맵에 담아 생성
        switch(registrationId) {
            case "naver":
                LinkedHashMap responseData = (LinkedHashMap) mod.get("response"); // response 객체가 넘어온다.
                mod.putAll(responseData); // 이러면 mod 로부터 1차로 데이터들을 받을 수 있다.
                mod.remove("response"); // 필요없는 response 는 지워준다.
                break;
            case "kakao":
                LinkedHashMap propertiesData = (LinkedHashMap) mod.get("properties"); // 카카오는 properties 안에 있다.
                mod.putAll(propertiesData);
                mod.remove("properties");
                break;
        }
        return mod;
    }
}