package com.minimi.core;

import org.springframework.stereotype.Component;

import java.util.Random;

public class RandomUtil {
    public static String generateRandomNumber(int length) {
        Random random = new Random();
        int createNum = 0;
        String ranNum = "";
        String resultNum = "";

        for (int i=0; i<length; i++) {
            createNum = random.nextInt(9);
            ranNum =  Integer.toString(createNum);
            resultNum += ranNum;
        }
        return resultNum;
    }
}
