package com.koreait.facebook_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 스프링부트 부터는 톰캣 없이 얘가 서버를 실행시켜준다.
public class FacebookCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacebookCloneApplication.class, args);
    }

}
