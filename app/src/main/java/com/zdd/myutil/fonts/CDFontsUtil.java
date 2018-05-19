package com.zdd.myutil.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by yd on 2018/5/18.
 */

public class CDFontsUtil {
    public static Typeface OCTICONS;

    /**
     * Get octicons typeface
     *
     * @param context
     * @return octicons typeface
     */
    public static Typeface getOcticons(final Context context) {
        if (OCTICONS == null)
            OCTICONS = getTypeface(context, "font/icomoon.ttf");
        return OCTICONS;
    }

    /**
     * Set octicons typeface on given text view(s)
     *
     * @param textViews
     */
    public static void setOcticons(final TextView... textViews) {
        if (textViews == null || textViews.length == 0)
            return;

        Typeface typeface = getOcticons(textViews[0].getContext());
        for (TextView textView : textViews)
            textView.setTypeface(typeface);
    }

    /**
     * Get typeface with name
     *
     * @param context
     * @param name
     * @return typeface
     */
    public static Typeface getTypeface(final Context context, final String name) {
        return Typeface.createFromAsset(context.getAssets(), name);
    }
}
