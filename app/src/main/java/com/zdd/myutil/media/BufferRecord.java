package com.zdd.myutil.media;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * create by zhudedian at 2018/6/23.
 */

public class BufferRecord {
    
    private RecordUtil recordUtil;
    private ByteBuffer buffer;
    private List<ByteBuffer> bufferList;
    private int position = 0;
    private int endPos = 0;
    private boolean isStart = false; //有声音输入，录音开始
    private boolean isEnd= false; //timeOut时间后没有声音输入，结束
    private boolean isReady = false;
    private boolean isDestroyed = false;

    private boolean stopRunable = false;
    private int timeOut = 1000;
    private Object lock;
    public BufferRecord(Object lock){
        this.recordUtil = RecordUtil.getInstance();
        this.lock = lock;
    }
    public BufferRecord start(){
        Log.i("edong", "start!!!!!!!");
        recordUtil.addRecordListener(recordListener);
        handler.postDelayed(stopRecordRunnable,10000);
        return this;
    }
    public boolean isStart(){
        return isStart;
    }
    public boolean isReady(){
        return isReady;
    }
    /**
     *  @Description 获取已经录好的那段音频
     *  @author zhudedian
     *  @time 2018/6/23  15:03
     */
    public ByteBuffer getBuffer(){
        while (!isReady){
            try {
                lock.wait(); // 还没录好，进入等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        buffer = ByteBuffer.allocateDirect(recordUtil.minBufferSize*(endPos));
        if (bufferList == null){
            return buffer;
        }
        for (int i = 0;i<endPos;i++){
            buffer.put(bufferList.get(i));
        }
        Log.i("edong", "return buffer;"+bufferList.size()+",endPos"+endPos);
        return buffer;
    }
    /**
     *  @Description 销毁，防止继续录音，影响性能
     *  @author zhudedian
     *  @time 2018/6/23  15:07
     */
    public void destroy(){
        isDestroyed = true;
        recordUtil.removeRecordListener(recordListener);
        handler.removeCallbacks(stopRecordRunnable);
    }
    private RecordUtil.RecordListener recordListener = new RecordUtil.RecordListener() {
        @Override
        public void read(ByteBuffer byteBuffer) {
            int volume = calculateVolume(byteBuffer.array(), 16);
            if (volume>1){
                if (!isStart) { //如果音量大于0，还没开始录音就开始录音
                    isStart = true;
                    Log.i("edong", "isStart!!!!!!!");
                }
//                UIHandler.getUiHandler().sendMessage(UIHandler.RECORDSTART);
            }
            if (isStart&&!isEnd&&!isDestroyed){
                if (bufferList==null){
                    bufferList = new ArrayList<>();
                }
                bufferList.add(byteBuffer);
                if (bufferList.size()>300){
                    isEnd = true;
                }
                if (volume < 1){
                    if (!stopRunable) {
                        stopRunable = true;
                        endPos = bufferList.size();
                        handler.postDelayed(stopRecordRunnable, timeOut);
                    }
                }else {
                    if (stopRunable) {
                        stopRunable = false;
                        handler.removeCallbacks(stopRecordRunnable);
                    }
                }
            }
            if (isEnd&&!isDestroyed){
                recordUtil.removeRecordListener(recordListener);
                String size;
                if (bufferList!=null){
                    size = bufferList.size()+"";
                }else {
                    size = "null";
                }
                Log.i("edong", "isEnd!!!!!!!"+size+",endPos"+endPos);
                isReady = true;
//                UIHandler.getUiHandler().sendMessage(UIHandler.RECORDEND);
                try {
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        }
    };
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable stopRecordRunnable = new Runnable() {
        @Override
        public void run() {
            isEnd = true;
        }
    };
    /**
     *  @Description 计算录音音量
     *  @author zhudedian
     *  @time 2018/6/23  10:22
     */
    public int calculateVolume(byte[] var0, int var1) {
        int[] var3 = null;
        int var4 = var0.length;
        int var2;
        if (var1 == 8) {
            var3 = new int[var4];
            for (var2 = 0; var2 < var4; ++var2) {
                var3[var2] = var0[var2];
            }
        } else if (var1 == 16) {
            var3 = new int[var4 / 2];
            for (var2 = 0; var2 < var4 / 2; ++var2) {
                byte var5 = var0[var2 * 2];
                byte var6 = var0[var2 * 2 + 1];
                int var13;
                if (var5 < 0) {
                    var13 = var5 + 256;
                } else {
                    var13 = var5;
                }
                short var7 = (short) (var13 + 0);
                if (var6 < 0) {
                    var13 = var6 + 256;
                } else {
                    var13 = var6;
                }
                var3[var2] = (short) (var7 + (var13 << 8));
            }
        }

        int[] var8 = var3;
        if (var3 != null && var3.length != 0) {
            float var10 = 0.0F;
            for (int var11 = 0; var11 < var8.length; ++var11) {
                var10 += (float) (var8[var11] * var8[var11]);
            }
            var10 /= (float) var8.length;
            float var12 = 0.0F;
            for (var4 = 0; var4 < var8.length; ++var4) {
                var12 += (float) var8[var4];
            }
            var12 /= (float) var8.length;
            var4 = (int) (Math.pow(2.0D, (double) (var1 - 1)) - 1.0D);
            double var14 = Math.sqrt((double) (var10 - var12 * var12));
            int var9;
            if ((var9 = (int) (10.0D * Math.log10(var14 * 10.0D * Math.sqrt(2.0D) / (double) var4 + 1.0D))) < 0) {
                var9 = 0;
            }
            if (var9 > 10) {
                var9 = 10;
            }
            return var9;
        } else {
            return 0;
        }
    }
}
