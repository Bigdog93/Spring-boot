package com.koreait.facebook_clone.feed.model;

import lombok.Data;

@Data
public class FeedDomain extends FeedEntity{
    private String writer;
    private String mainProfile;
    private int ifeedImg;
    private String img;
}
