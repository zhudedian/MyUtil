package com.zdd.myutil.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.zdd.myutil.R;
import com.zdd.myutil.view.icon.FreshView;

/**
 * Created by yd on 2018/5/17.
 */

public class TestView extends View {

    private Paint mPaint;
    private float paintWidth = 3.5f;
    private float width,height;
    private float paddingLeft,paddingRight,paddingTop,paddingBottom;
    private int color = 0;
    private String textStr = "1";

    public TestView(Context context,int color,String text){
        this(context,null);
        this.color = color;
        this.textStr = text;
    }
    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.freshview));
        mPaint.setAlpha(255);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(paintWidth);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(40);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//没有
        mPaint.setStrokeJoin(Paint.Join.BEVEL);//直线

    }


    private void init() {
        paddingLeft = getPaddingLeft()+paintWidth;
        paddingRight = getPaddingRight()+paintWidth;
        paddingTop = getPaddingTop()+paintWidth;
        paddingBottom = getPaddingBottom()+paintWidth;

        width = getWidth()-paddingLeft-paddingRight;
        height = getHeight()-paddingTop-paddingBottom;

        setBackgroundColor(color);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        canvas.translate(paddingLeft, paddingTop);
        canvas.drawText(textStr,width/2,height/2,mPaint);


    }
}
