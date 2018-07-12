package com.zdd.myutil.media;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * create by zhudedian at 2018/6/22.
 */

public class RecordUtil {

    private static RecordUtil recordUtil;
    private AudioRecord audioRecord;
    public int minBufferSize;
    private final int encoding = AudioFormat.ENCODING_PCM_16BIT;
    private final int chan = AudioFormat.CHANNEL_IN_MONO;
    private final int rate = 16000;
    private final int recsrc = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) ? MediaRecorder.AudioSource.VOICE_RECOGNITION
            : MediaRecorder.AudioSource.DEFAULT;
    private boolean isRecording = false;

    public RecordUtil(){
        minBufferSize = AudioRecord.getMinBufferSize(rate, chan, AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(recsrc, rate,chan, encoding, minBufferSize );
    }
    public static RecordUtil getInstance(){
        if (recordUtil == null){
            recordUtil = new RecordUtil();
        }
        return recordUtil;
    }
    
    /**
     *  @Description 开始录音
     *  @author zhudedian
     *  @time 2018/6/26  19:40
     */
    public RecordUtil startRecording(){

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    audioRecord.startRecording();
                    Log.i("edong","开始录音");
                    isRecording = true;
                    while (isRecording){
                        ByteBuffer buffer = ByteBuffer.allocateDirect(minBufferSize);
                        audioRecord.read(buffer,minBufferSize);
                        if (recordListenerList!=null){
                            for (int i = 0; i<recordListenerList.size();i++){
                                RecordListener listener = recordListenerList.get(i);
                                if (listener!=null) {
                                    listener.read(buffer);
                                }
                            }
                        }
                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }

        return recordUtil;
    }


    public boolean isRecording(){
        return isRecording;
    }
    public void stopRecording(){
        isRecording = false;
        audioRecord.stop();
        audioRecord.release();

    }




    private List<RecordListener> recordListenerList;
    public void addRecordListener(RecordListener listener){
        if (recordListenerList == null){
            recordListenerList = new ArrayList<>(1);
        }
        if (!recordListenerList.contains(listener)){
            recordListenerList.add(listener);
        }
    }
    public void removeRecordListener(RecordListener listener){
        if (recordListenerList!=null){
            recordListenerList.remove(listener);
        }
    }
    public interface RecordListener{
        void read(ByteBuffer byteBuffer);
    }
}
