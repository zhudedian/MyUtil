package com.zdd.myutil.alarm;


import java.util.List;

/**
 * Created by dyx on 2018/1/4.
 * 对讯飞返回的结果的封装
 */

public class ResultBean {

    // 需要进行播放的类型
    public enum PlayType {
        NO, HINT, MUSIC, CROSS_TALK, STORY, ENGLISH, NEWS, NEXT, PAST, PAUSE, RESUME, STOP,
        SLEEP_MODE, BLUETOOTH_OPEN, BLUETOOTH_CLOSE,CLOCK
    }

    private String tts;                             // 语音反馈TTS
    private PlayType playType;                      // 播放类型
    private List resList;                           // 资源集合
    private int position;                           // 播放集合中音乐的位置(第几首)


    public ResultBean(String tts, PlayType playType, List resList, int position) {
        this.tts = tts;
        this.playType = playType;
        this.resList = resList;
        this.position = position;
    }

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }

    public PlayType getPlayType() {
        return playType;
    }

    public void setPlayType(PlayType playType) {
        this.playType = playType;
    }

    public List getResList() {
        return resList;
    }

    public void setResList(List resList) {
        this.resList = resList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}