package com.koreait.facebook_clone.common.auth;

import org.springframework.stereotype.Component;


@Component
public class RandomCodeGenerator {

    public int getRandomInteger(int min, int max) {
        return (int) ((Math.random() * (max - min + 1)) + min);
    }

    public int getRandomInteger(int max) {
        return (int) (Math.random() * (max + 1));
    }
    public char getRandomChar() {
        while (true) {
            char result = (char) getRandomInteger(65, 122);
            if (result < 91 || result > 96){
                return result;
            }
        }
    }

    // len 길이 만큼의 랜덤한 숫자를 문자열로 리턴
    public String getRandomCode(int len) {
        StringBuilder randVal = new StringBuilder();
        for(int i = 0; i < len; i++) {
            int rand = getRandomInteger(9);
            randVal.append(rand);
        }
        return randVal.toString();
    }
}
