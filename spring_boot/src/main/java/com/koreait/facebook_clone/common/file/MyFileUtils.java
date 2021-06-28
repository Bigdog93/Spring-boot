package com.koreait.facebook_clone.common.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
public class MyFileUtils {

    @Value("${spring.servlet.multipart.location}") // 문자열로 들어간다.
    private String uploadImagePath;

    // 폴더만들기
    public boolean makeFolders(String path) {
        File folder = new File(path);
        return folder.mkdirs();
    }

    // 저장 경로 만들기
    public String getSavePath(String path) {
        return uploadImagePath + "/" + path;
    }

    // 랜덤 파일명 만들기
    public String getRandomFileNm() {
        return UUID.randomUUID().toString();
    }

    // 랜덤 파일명 만들기 (with확장자)         "aaa.jpg"
    public String getRandomFileNm(String originFileNm) {
        return getRandomFileNm() + "." + getExt(originFileNm);
    }

    // 랜덤 파일명 만들기
    public String getRandomFileNm(MultipartFile file) {
        return getRandomFileNm(file.getOriginalFilename());
    }

    // 확장자 얻기
    public String getExt(String fileNm) {
        return fileNm.substring(fileNm.lastIndexOf(".") + 1);
    } // 끝에서 . 뒤 글자의 인덱스를 return, 거기서 부터 끝까지 잘라냄

    // 파일 저장 & 랜덤파일명 리턴
    public String transferTo(MultipartFile mf, String target) { // target = "profile/userPk"
        // mf 는 넘어온 이미지를 차례대로 넣어줄 곳
        // target 은 profile/10 처럼 기존 경로 이후의 경로
        String fileNm = getRandomFileNm(mf); // "a*f3%m4M4i4*8j4!fm^m.jpg"
        String basePath = getSavePath(target); // 이미지를 저장할 절대경로값을 만든다. "c:/springRes/facebookClone/..."
        makeFolders(basePath); // (폴더가 없을 수 있기 때문에) 폴더를 만들어 준다.
        File saveFile = new File(basePath, fileNm);
        try {
            mf.transferTo(saveFile);
            return fileNm;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
