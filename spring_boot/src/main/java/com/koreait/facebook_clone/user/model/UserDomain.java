package com.koreait.facebook_clone.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserDomain extends UserEntity{
    private int cntFeed;
    private int cntFollower;
    private int cntFollow;
    private int isYouFollowMe;
    private int isMeFollowYou;
}
