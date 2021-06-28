package com.koreait.facebook_clone.feed;

import com.koreait.facebook_clone.feed.model.FeedEntity;
import com.koreait.facebook_clone.feed.model.FeedImgEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedMapper {
    int insFeed(FeedEntity param);
    int insFeedImg(FeedImgEntity param);
}
