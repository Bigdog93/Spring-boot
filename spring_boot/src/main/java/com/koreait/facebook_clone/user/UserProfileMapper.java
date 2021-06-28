package com.koreait.facebook_clone.user;

import com.koreait.facebook_clone.user.model.UserEntity;
import com.koreait.facebook_clone.user.model.UserProfileEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserProfileMapper {
    int insUserProfile(UserProfileEntity param);
    List<UserProfileEntity> selUserProfileList(UserEntity param);
}
