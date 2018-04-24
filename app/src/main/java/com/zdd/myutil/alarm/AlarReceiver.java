package com.zdd.myutil.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //每次接受广播去扫描一次本地闹钟
//        Log.i("edong","每次接受广播去扫描一次本地闹钟！");
        new Thread(){
            public void run(){
                AlarUtil.initAlar(context);
            }
        }.start();

    }
}
