package com.zdd.myutil.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.StringDef;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

/**
 * Created by yd on 2018/6/1.
 */

public class MediaUtil {

    private static Context context;
    private static MediaPlayer mediaPlayer;
    private static boolean mIsIntialized = false;
    private static TextToSpeech mTextToSpeech;
    public static final String DISTURBOPEN = "mp3/disturb_open.mp3";
    public static final String DISTURBCLOSE = "mp3/disturb_close.mp3";
    @StringDef({DISTURBOPEN,DISTURBCLOSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Assets{}

    public static void init(Context context){
        MediaUtil.context = context;
        mTextToSpeech = new TextToSpeech(context, mInitListener);
//        mTextToSpeech.setPitch(.8f);
//        mTextToSpeech.setSpeechRate(1.5f);
        mTextToSpeech.setLanguage(Locale.US);
//        mTextToSpeech.setLanguage(Locale.CHINA);
    }
    public static void play(@Assets String url){
        Log.i("edong","MediaUtil.play,"+url);
        // 如果正在播放其他提示音 先停掉
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.release();
            } catch (Exception e) {
            }
        }
        mediaPlayer = new MediaPlayer();
        AssetManager assetManager = context.getAssets();
        try {
            AssetFileDescriptor fileDescriptor = assetManager.openFd(url);
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
    public static void play(File file){
        Log.i("edong","MediaUtil.play,"+file.getAbsolutePath());
        // 如果正在播放其他提示音 先停掉
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.release();
            } catch (Exception e) {
            }
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
    public static void speech(String text){
        Log.i("edong","mIsIntialized="+mIsIntialized);
        if (mIsIntialized){
            mTextToSpeech.speak(text,TextToSpeech.QUEUE_ADD,null);
        }
    }

    private static TextToSpeech.OnInitListener mInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if(status == TextToSpeech.SUCCESS){
                Log.i("edong","TextToSpeech.SUCCESS,");
                mIsIntialized = true;
            }else{
                new IllegalStateException("Unable to initialize Text to Speech engine").printStackTrace();
            }
        }
    };
}
