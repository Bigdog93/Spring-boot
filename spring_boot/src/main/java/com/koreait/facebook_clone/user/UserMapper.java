package com.koreait.facebook_clone.user;

import com.koreait.facebook_clone.user.model.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int join(UserEntity param);
}
