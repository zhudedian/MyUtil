package com.zdd.myutil.system.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;

/**
 * create by zhudedian on 2018/7/4.
 */

public class WindowUtil {

    /**
     *  @Description 隐藏虚拟按键
     *  @author zhudedian
     *  @time 2018/7/4  17:07
     */
    public static void dismissDecorView(Activity activity){
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    /**
     *  @Description 显示虚拟按键
     *  @author zhudedian
     *  @time 2018/7/4  17:08
     */
    public static void showDecorView(Activity activity){
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            //低版本sdk
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.VISIBLE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
