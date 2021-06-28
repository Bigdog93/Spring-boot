package com.koreait.facebook_clone.feed;

import com.koreait.facebook_clone.feed.model.FeedEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/feed")
public class FeedController {

    @GetMapping("/home")
    public void home() {}

    @GetMapping("/reg")
    public void reg(@ModelAttribute("feedEntity") FeedEntity obj) {}

}
