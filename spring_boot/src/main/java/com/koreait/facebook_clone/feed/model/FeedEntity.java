package com.koreait.facebook_clone.feed.model;

import lombok.Data;

@Data
public class FeedEntity {
    private int ifeed;
    private int iuser;
    private String location;
    private String ctnt;
    private String regdt;
}
