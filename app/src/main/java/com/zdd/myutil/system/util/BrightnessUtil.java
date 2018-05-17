package com.zdd.myutil.system.util;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;



/**
 * Created by yd on 2018/5/5.
 */

public class BrightnessUtil {

    private static BrightChangeListener changeListener;
    public static int getBrightness(Context context) {
        int screenBrightnessValue= SharedPreferencesUtils.getScreenBrightnessValue(context,175);
        setScreenBrightness(context,screenBrightnessValue);
        return screenBrightnessValue;
    }
    public static void setScreenBrightness(Context context, int progress) {
        try {
            Window window = ((Activity)context).getWindow();
            WindowManager.LayoutParams params = window
                    .getAttributes();
            params.screenBrightness = (progress / 255.0F);
            window.setAttributes(params);
            SharedPreferencesUtils.saveScreenBrightnessValue(context,progress);
            if (changeListener!=null){
                changeListener.onChange(progress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setBrightChangeListener(BrightChangeListener listener){
        changeListener = listener;
    }
    public interface BrightChangeListener{
        void onChange(int progress);
    }
}
