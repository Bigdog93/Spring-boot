package com.koreait.facebook_clone.user.model;

import lombok.Data;

@Data
public class UserFollowEntity {
    private int iuserFrom;
    private int iuserTo;
    private String regdt;
}
