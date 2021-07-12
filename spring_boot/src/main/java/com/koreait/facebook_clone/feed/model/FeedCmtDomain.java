package com.koreait.facebook_clone.feed.model;

import lombok.Data;

@Data
public class FeedCmtDomain extends FeedCmtEntity{
    private String writer;
    private String writerProfile;
    private int isMore; // 2: 더있음
}
