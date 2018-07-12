package com.zdd.myutil.system.util;

import android.media.AudioManager;



/**
 * create by zhudedian on 2018/6/29.
 */

public class VolumeUtil {

    /**
     *  @Description 保存闹铃音量
     *  @author zhudedian
     *  @time 2018/6/29  15:01
     */
    public static void setAlertVolume(int volume){
        setVolume(AudioManager.STREAM_RING,volume,100);
    }
    /**
     *  @Description 获取闹铃音量
     *  @author zhudedian
     *  @time 2018/6/29  15:01
     */
    public static int getAlertVolume(){
        return AudioUtil.getAudioUtil().getStreamVolume(AudioManager.STREAM_RING);
    }

    /**
     *  @Description 保存说话音量
     *  @author zhudedian
     *  @time 2018/6/29  15:01
     */
    public static void setSpeakerVolume(int volume){
        setVolume(AudioManager.STREAM_MUSIC,volume,100);
        sendVolumeEvent();
    }
    /**
     *  @Description 获取说话音量
     *  @author zhudedian
     *  @time 2018/6/29  15:01
     */
    public static int getSpeakerVolume(){
        return AudioUtil.getAudioUtil().getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static int getMaxVolume(int streamType){
        return AudioUtil.getAudioUtil().getStreamMaxVolume(streamType);
    }

    /**
     *  @Description 设置现在的音量
     *  @author zhudedian
     *  @time 2018/6/29  15:10
     */
    public static void setVolume(int streamType,int volume,int maxVolume){
        AudioUtil audioUtil = AudioUtil.getAudioUtil();
        int max = audioUtil.getStreamMaxVolume(streamType);
        audioUtil.setStreamVolume(streamType,volume*max/maxVolume);
    }
    public static void sendVolumeEvent(){
        int vol = getSpeakerVolume();
//        SpeechUtil.sendEvent(Event.getVolumeChangedEvent(vol*100/15, vol == 0));
    }

    public static void addVolumeChangeListener(AudioUtil.VolumeChangeListener listener){
        AudioUtil.getAudioUtil().addVolumeChangeListener(listener);
    }
    public static void removeVolumeChangeListener(AudioUtil.VolumeChangeListener listener){
        AudioUtil.getAudioUtil().removeVolumeChangeListener(listener);
    }
}
