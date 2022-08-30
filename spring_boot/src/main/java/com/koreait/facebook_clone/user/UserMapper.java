package com.koreait.facebook_clone.user;

import com.koreait.facebook_clone.user.model.UserDomain;
import com.koreait.facebook_clone.user.model.UserEntity;
import com.koreait.facebook_clone.user.model.UserFollowEntity;
import com.koreait.facebook_clone.user.model.UserProfileEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    int join(UserEntity param);
    int auth(UserEntity param);
    UserDomain selUser(UserEntity param);
    int updUserProfile(UserEntity param);
    int updUserMainProfile(UserProfileEntity param);

    int insUserFollow(UserFollowEntity param);
    UserFollowEntity selUserFollow(UserFollowEntity param);
    List<UserDomain> selUserFollowList(UserFollowEntity param);
    List<UserDomain> selUserFollowerList(UserFollowEntity param);
    int delUserFollow(UserFollowEntity param);
}
