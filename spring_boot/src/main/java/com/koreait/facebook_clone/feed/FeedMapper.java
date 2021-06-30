package com.koreait.facebook_clone.feed;

import com.koreait.facebook_clone.feed.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedMapper {
    int insFeed(FeedEntity param);
    int insFeedImg(FeedImgEntity param);
    List<FeedDomain> selFeedList();
    List<FeedDomain2> selFeedList2(FeedDTO param);
    List<FeedImgEntity> selFeedImgList(FeedDomain2 param);
}
