package com.koreait.facebook_clone.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration // 웹 설정 파일
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${spring.servlet.multipart.location}") // "C:\SpringRes\facebookClone"
    private String uploadImageSPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 프로젝트 외부에 있는 파일을 어떤 경로로 들어왔을때 그 폴더로 연결 시켜 준다.
        registry.addResourceHandler("/pic/**") // Handler : 핸들. 주소창에 치는 값
                .addResourceLocations("file:///" + uploadImageSPath + "/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    } // /pic/profile/3/93c48e62-6870-43f4-a0b8-228925a96e53.jpg 로 검색하면 나온다.

}
