package com.koreait.facebook_clone.feed;

import com.koreait.facebook_clone.common.file.MyFileUtils;
import com.koreait.facebook_clone.common.security.MySecurityUtils;
import com.koreait.facebook_clone.feed.model.*;
import com.koreait.facebook_clone.security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FeedService {

    @Autowired
    private FeedMapper mapper;

    @Autowired
    private IAuthenticationFacade auth;

    @Autowired private MyFileUtils fileUtils;

    public int regFeed(MultipartFile[] imgArr, FeedEntity param) {
        if(imgArr == null && param.getCtnt() == null) {
            //더블 체크. 프론트 쪽에서 null 이면 submit 못하게 했지만
            // 장난질 치는 경우가 있으므로 백쪽에도 막아주자ㅏ.
            return 0;
        }
        param.setIuser(auth.getLoginUserPk());
        // t_feed 부터 insert
        int result = mapper.insFeed(param);
        if(result > 0 && imgArr != null && imgArr.length > 0) {
            // 이미지 저장
            FeedImgEntity imgEntity = new FeedImgEntity();
            imgEntity.setIfeed(param.getIfeed());
            String target = "feed/" + param.getIfeed();
            for(MultipartFile img : imgArr) {
                String saveFileNm = fileUtils.transferTo(img, target);

                if(saveFileNm != null) { // 이미지 정보 DB에 저장
                    imgEntity.setImg(saveFileNm);
                    mapper.insFeedImg(imgEntity);
                }
            }
        }
        return result;
    }

    public List<FeedDomain> selFeedList() {
        return mapper.selFeedList();
    }
    public List<FeedDomain2> selFeedList2(FeedDTO param) {
        return mapper.selFeedList2(param);
    }
}
