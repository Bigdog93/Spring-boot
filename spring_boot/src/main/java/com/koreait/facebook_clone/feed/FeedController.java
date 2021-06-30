package com.koreait.facebook_clone.feed;

import com.koreait.facebook_clone.common.MyConst;
import com.koreait.facebook_clone.feed.model.FeedDTO;
import com.koreait.facebook_clone.feed.model.FeedDomain;
import com.koreait.facebook_clone.feed.model.FeedDomain2;
import com.koreait.facebook_clone.feed.model.FeedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/feed")
public class FeedController {

    @Autowired
    private FeedService service;

    @Autowired
    private MyConst myConst;


    @GetMapping("/home")
    public void home() {}

    @GetMapping("/reg")
    public void reg(@ModelAttribute("feedEntity") FeedEntity obj) {}

    @PostMapping("/reg")
    @ResponseBody
    public Map<String, Integer> reg(MultipartFile[] imgArr, FeedEntity param) {
        // data 의 key 값과 같은 변수명을 사용하면 알아서 받는다..?
        // 이름 다르면 @ModelAttribute() 가져올 name 값 명시!!
        Map<String, Integer> res = new HashMap<>();
        res.put(myConst.RESULT, service.regFeed(imgArr, param));
        return res;
    }

    @ResponseBody
    @GetMapping("/list")
    public List<FeedDomain> selFeed() {
        return service.selFeedList();
    }

    @ResponseBody
    @GetMapping("/list2")
    public List<FeedDomain2> selFeed2(FeedDTO param) {
        return service.selFeedList2(param);
    }
}
