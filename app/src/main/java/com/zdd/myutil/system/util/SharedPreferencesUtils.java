package com.zdd.myutil.system.util;




import android.content.Context;


public class SharedPreferencesUtils {

	public static void saveScreenBrightnessValue(Context context, int screenBrightnessValue) {
		context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit()
				.putInt("screen_brightness", screenBrightnessValue).commit();
	}

	public static int getScreenBrightnessValue(Context context, int defValue) {
		return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE)
				.getInt("screen_brightness", defValue);
	}
}
