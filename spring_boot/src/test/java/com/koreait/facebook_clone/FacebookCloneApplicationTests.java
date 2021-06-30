package com.koreait.facebook_clone;

import com.koreait.facebook_clone.common.mailsender.EmailService;
import com.koreait.facebook_clone.common.security.MySecurityUtils;
import com.koreait.facebook_clone.feed.FeedMapper;
import com.koreait.facebook_clone.feed.model.FeedEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class FacebookCloneApplicationTests {

    @Autowired
    private EmailService emailService;

    @Autowired
    private MySecurityUtils securityUtils;

    @Autowired
    private FeedMapper feedMapper;

    @Test
    void getRandVal() {
        FeedEntity param = new FeedEntity();
        param.setCtnt("ii");
        param.setIuser(3);
        int result = feedMapper.insFeed(param);
        System.out.println(param);

    }

}
