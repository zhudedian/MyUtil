package com.zdd.myutil.alarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yd on 2018/4/10.
 */

public class DateUtil {
    /**
     * 获取具体时间在某一天，今天、明天、后天等
     * */
    public static String getDateStrForTime(long time){
        long curStartTime = getCurrentDayStartTime();
            if(time<(curStartTime+3600000*24)){
                return "今天";
            }else if(time<(curStartTime+3600000*48)){
                return "明天";
            }else if(time<(curStartTime+3600000*72)){
                return "后天";
            }else {
                SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date(time);
                String curTime = format.format(curDate);
                String dateStr = curTime.split(" ")[0];
                String[] dates = dateStr.split("-");
                return dates[1]+"月"+dates[2]+"日";
            }
    }
    /**
     * 将时间转换成年月日格式
     * */
    public static String formatTime(long time){
        SimpleDateFormat format= new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(time);
        String curTime = format.format(curDate);
        return curTime;
    }

    /**
     * 根据日期与时间来获取最近的时间long
     * */
    public static long getNextLatelyTime(int startMonth,int day,long time){
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(time);
        String curTime = format.format(curDate);
        return getNextLatelyTime(startMonth,day,curTime);
    }
    /**
     * 根据日期与时间来获取最近的时间long
     * */
    public static long getNextLatelyTime(int startMonth,int day,String time){

        int i = startMonth;
        while (day>getDaysNextMonth(i)){
            i++;
        }
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String curTime = format.format(curDate);
        String dateStr = curTime.split(" ")[0];
        String[] dates = dateStr.split("-");
        int year = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        month = month+i;
        if ((month+i)>12){
            year = year+(int)((month+i)/12);
            month = (month+i)%12;
        }
        String timeStr = time.split(" ")[1];
        curTime = year+"-"+month+"-"+day+" "+timeStr;
        Date date = null;
        try {
            date = format.parse(curTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date!=null){
            return date.getTime();
        }

        return 0;
    }
    /**
     * 获取具体时间在某一时间段
     * */
    public static String getIntervalForTime(long time){
        SimpleDateFormat format= new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(time);
        String curTime = format.format(curDate);
        String startTime = curTime.split(" ")[0]+" 00:00:00";
        Date date = null;
        try {
            date = format.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date!=null){
            if(time<(date.getTime()+3600000*6)){
                return "凌晨";
            }else if(time<(date.getTime()+3600000*8)){
                return "早晨";
            }else if(time<(date.getTime()+3600000*12)){
                return "上午";
            }else if(time<(date.getTime()+3600000*14)){
                return "中午";
            }else if(time<(date.getTime()+3600000*17)){
                return "下午";
            }else if(time<(date.getTime()+3600000*19)){
                return "傍晚";
            }else if(time<(date.getTime()+3600000*24)){
                return "晚上";
            }
        }
        return "";
    }
    /**
     * 获取当天零点的long时间
     * */
    public static long getCurrentDayStartTime(){
        SimpleDateFormat format= new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String curTime = format.format(curDate);
        String startTime = curTime.split(" ")[0]+" 00:00:00";
        Date date = null;
        try {
            date = format.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date!=null){
            return date.getTime();
        }
        return 0;
    }
    /**
     * 将数字转换成周几
     * */
    public static String changeToWeekStr(String weekStr) {
        int week = Integer.parseInt(weekStr);
        if (week<1||week>7){
            return "";
        }
        if (week==7){
            return "周日";
        }
        return "周"+NumUtil.getNumberStr(week);
    }
    /**
     * 获取下一个月的天数，0是当前月
     * */
    public static int getDaysNextMonth(int month) {
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String curTime = format.format(curDate);
        String dateStr = curTime.split(" ")[0];
        String[] dates = dateStr.split("-");
        int year = Integer.parseInt(dates[0]);
        int curMonth = Integer.parseInt(dates[1]);
        int yearAdd = (int) (curMonth+month)/12;
        int nextMonth = (int) (curMonth+month)%12;
        year = year+yearAdd;
        return getDaysByYearMonth(year,nextMonth);
    }
    /**
     * 获取当月的 天数
     * */
    public static int getCurrentMonthDay() {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
    /**
     * 根据年 月 获取对应的月份 天数
     * */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据日期 找到对应日期的 星期
     */
    public static String getDayOfWeekByDate(String date) {
        String dayOfweek = "-1";
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = myFormatter.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("E");
            String str = formatter.format(myDate);
            dayOfweek = str;

        } catch (Exception e) {
            System.out.println("错误!");
        }
        return dayOfweek;
    }
}
