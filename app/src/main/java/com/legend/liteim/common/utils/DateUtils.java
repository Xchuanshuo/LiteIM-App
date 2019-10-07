package com.legend.liteim.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
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
//        Date date = new Date();
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(getHourDiff(date, new Date()));
        String date1 = "2019-09-28 01:20:46";
        String date2 = "2019-09-25 00:55:36";
        System.out.println(compare(date1, date2));
    }

    public static long getHourDiff(Date date1, Date date2) {
        System.out.println(date1.getTime());
        System.out.println(date2.getTime());
        long diff = (date2.getTime() - date1.getTime())/(3600*1000);
        return diff;
    }

    /**
     * 比较第一个时间是否晚于第二个时间
     * @param format1
     * @param format2
     * @return
     */
    public static boolean compare(String format1, String format2) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = dateFormat.parse(format1);
            Date date2 = dateFormat.parse(format2);
            return date1.compareTo(date2) >= 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *  当前时间
     */
    public static String getDate() {
//        // 之前版本
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // java8写法
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//        LocalDate localDate = LocalDate.now();
//        String now_date = localDate.format(dateTimeFormatter);
        return dateFormat.format(date);
    }

    /**
     * 时间装换为毫秒
     * @return
     */
    public static long timeToMs(int n) {
        return n * 24 * 60 * 60 * 1000;
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
