package com.zhy.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by huangminzheng on 15/12/3.
 */
public class HmzUtils {

    /**
     * 获取随机数
     * @param count 随机数的个数
     * @return
     */
    public static String randomNumStr(int count) {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();//Set能去掉重复的元素并获取相应数据
        while (set.size() < count) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set) {
            sb.append("" + i);
        }

        return sb.toString();
    }

    /**
     * 获取随机数
     * @param count 随机数的个数
     * @return
     */
    public static int randomNumInt(int count) {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();//Set能去掉重复的元素并获取相应数据
        while (set.size() < count) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set) {
            sb.append("" + i);
        }

        return Integer.parseInt(sb.toString());
    }
}
