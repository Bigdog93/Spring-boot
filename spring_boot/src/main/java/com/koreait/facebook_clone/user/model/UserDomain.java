package com.koreait.facebook_clone.user.model;

import lombok.Data;

@Data
public class UserDomain extends UserEntity{
    private int cntFeed;
    private int cntFollower;
    private int cntFollow;
    private int isYouFollowMe;
    private int isMeFollowYou;
}
