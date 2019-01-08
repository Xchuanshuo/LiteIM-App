package com.worldtreestd.finder.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author Legend
 * @data by on 2017/12/5.
 * @description 时间日期工具类
 */

public class DateUtils {

    public static void main(String[] args) {
        Date date = new Date();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(getHourDiff(date, new Date()));
    }

    public static long getHourDiff(Date date1, Date date2) {
        System.out.println(date1.getTime());
        System.out.println(date2.getTime());
        long diff = (date2.getTime() - date1.getTime())/(3600*1000);
        return diff;
    }

    /**
     *  当前时间
     */
    public static String getDate() {
//        // 之前版本
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String now_date = dateFormat.format(date);
        // java8写法
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//        LocalDate localDate = LocalDate.now();
//        String now_date = localDate.format(dateTimeFormatter);
        return now_date;
    }

    /**
     *  生成随机字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i < length;i++) {
            int number = random.nextInt(61);
            stringBuilder.append(str.charAt(number));
        }
        return stringBuilder.toString();
    }
}
