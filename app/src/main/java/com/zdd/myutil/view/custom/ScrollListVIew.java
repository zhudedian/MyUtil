package com.zdd.myutil.view.custom;

import android.content.Context;
import android.widget.ListView;

/**
 * Created by yd on 2018/5/5.
 */

public class ScrollListVIew extends ListView {
    public ScrollListVIew(Context context) {
        super(context);
    }

    public ScrollListVIew(Context context, android.util.AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}
