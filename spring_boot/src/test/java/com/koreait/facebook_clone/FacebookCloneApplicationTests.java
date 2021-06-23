package com.koreait.facebook_clone;

import com.koreait.facebook_clone.common.mailsender.EmailService;
import com.koreait.facebook_clone.common.security.MySecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class FacebookCloneApplicationTests {

    @Autowired
    private EmailService emailService;

    @Autowired
    private MySecurityUtils securityUtils;

    @Test
    void getRandVal() {
        int len = 5;
        String val = securityUtils.getRandomCode(len);
        assertEquals(val.length(), len);

        String val2 = securityUtils.getRandomCode(len);
        assertEquals(val2.length(), len);

        assertNotEquals(val, val2);

        System.out.println("val : " + val);
        System.out.println("val2 : " + val2);

    }

}
