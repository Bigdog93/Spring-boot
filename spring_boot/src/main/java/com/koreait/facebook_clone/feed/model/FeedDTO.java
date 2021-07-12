package com.koreait.facebook_clone.feed.model;

import lombok.Data;

@Data
public class FeedDTO {
    private int page;
    private int limit;
    private int iuserForMyFeed; // 나의 feed 만 보고싶을 때 쓰는
    private int iuserForFav; // feed 에 좋아요 여부 알려고 쓰는

    public int getStartIdx() {
        return (page - 1) * limit;
    } // 이렇게 getter 오버라이딩 가능
}
