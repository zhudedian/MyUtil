package com.zdd.myutil.alarm;

import org.litepal.crud.DataSupport;

/**
 * Created by Done on 2018/3/26.
 */

public class AlarEvent extends DataSupport {

    private int alarType;
    private long eventTime;
    private String interval;
    private String datetime_date;
    private String datetime_time;
    private String timeStr;
    private String event;
    private String value;
    private String repeatType;
    private String repeatDay;



    public AlarEvent(int alarType,long eventTime, String timeStr, String event,String value,
                     String repeatType,String interval){
        this.alarType = alarType;
        this.eventTime = eventTime;
        this.timeStr = timeStr;
        this.event = event;
        this.value = value;
        this.repeatType = repeatType;
        this.interval = interval;
    }
    public AlarEvent(int alarType,long eventTime, String timeStr, String event,String value,
                     String repeatType,String interval,String datetime_date,String datetime_time){
        this.alarType = alarType;
        this.eventTime = eventTime;
        this.timeStr = timeStr;
        this.event = event;
        this.value = value;
        this.repeatType = repeatType;
        this.interval = interval;
        this.datetime_date = datetime_date;
        this.datetime_time = datetime_time;
    }
    public void setAlarType(int alarType){
        this.alarType = alarType;
    }
    public int getAlarType(){
        return alarType;
    }
    public void setEvnetTime(long eventTime){
        this.eventTime = eventTime;
    }
    public long getEvnetTime(){
        return eventTime;
    }
    public void setTimeStr(String timeStr){
        this.timeStr = timeStr;
    }
    public String getTimeStr(){
        return timeStr;
    }
    public void setEvent(String event){
        this.event = event;
    }
    public String getEvent(){
        return event;
    }
    public void setValue(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }
    public void setRepeatType(String repeatType){
        this.repeatType = repeatType;
    }
    public String getRepeatType(){
        return repeatType;
    }
    public void setInterval(String interval){
        this.interval = interval;
    }
    public String getInterval(){
        return interval;
    }
    public void setDatetime_date(String datetime_date){
        this.datetime_date = datetime_date;
    }
    public String getDatetime_date(){
        return datetime_date;
    }
    public void setDatetime_time(String datetime_time){
        this.datetime_time = datetime_time;
    }
    public String getDatetime_time(){
        return datetime_time;
    }
    public void setRepeatDay(String repeatDay){
        this.repeatDay = repeatDay;
    }
    public String getRepeatDay(){
        return repeatDay;
    }

    public boolean equally(long evnetTime,String event,String repeatType){
        if (this.eventTime==evnetTime&&this.repeatType.equals(repeatType)&&this.event.equals(event)){
            return true;
        }
        return false;
    }
    public boolean equallyTime(long evnetTime){
        if (this.eventTime==evnetTime){
            return true;
        }
        return false;
    }
    @Override
    public boolean equals(Object object){
        if (object instanceof AlarEvent){
            AlarEvent alarEvent= (AlarEvent) object;
            if (alarEvent.getEvnetTime()==this.eventTime){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    @Override
    public int hashCode(){
        return 2;
    }

}
