package com.koreait.facebook_clone.feed.model;

import lombok.Data;

@Data
public class FeedCmtEntity {
    private int icmt;
    private int iuser;
    private int ifeed;
    private String cmt;
    private String regdt;
}
