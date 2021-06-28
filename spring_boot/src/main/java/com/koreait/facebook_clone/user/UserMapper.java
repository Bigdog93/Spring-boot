package com.koreait.facebook_clone.user;

import com.koreait.facebook_clone.user.model.UserDomain;
import com.koreait.facebook_clone.user.model.UserEntity;
import com.koreait.facebook_clone.user.model.UserProfileEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int join(UserEntity param);
    int auth(UserEntity param);
    UserDomain selUser(UserEntity param);
    int updUserProfile(UserEntity param);
    int updUserMainProfile(UserProfileEntity param);
}
