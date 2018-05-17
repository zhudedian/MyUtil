package com.zdd.myutil.view.icon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zdd.myutil.R;


/**
 * Created by yd on 2018/5/8.
 */

public class RoundCheck extends View {

    private Paint mPaint;
    private float radius1, radius2;
    private float paintWidth = 3.5f;
    private float width,height;
    private float paddingLeft,paddingRight,paddingTop,paddingBottom;
    private boolean isCheck = true;

    public RoundCheck(Context context) {
        this(context, null);
    }

    public RoundCheck(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCheck(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(255);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(paintWidth);
        mPaint.setStrokeCap(Paint.Cap.BUTT);//没有
        mPaint.setStrokeJoin(Paint.Join.BEVEL);//直线

    }

    public void setCheck(boolean check){
        this.isCheck = check;
        postInvalidate();
    }
    private void init() {
        paddingLeft = getPaddingLeft()+paintWidth/2;
        paddingRight = getPaddingRight()+paintWidth/2;
        paddingTop = getPaddingTop()+paintWidth/2;
        paddingBottom = getPaddingBottom()+paintWidth/2;

        width = getWidth()-paddingLeft-paddingRight;
        height = getHeight()-paddingTop-paddingBottom;

        radius1 = width/2;
        radius2 = width*22/100;

        if (isCheck){
            mPaint.setColor(getResources().getColor(R.color.round_check));
        }else {
            mPaint.setColor(Color.WHITE);
        }

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        canvas.translate(paddingLeft+width/2, paddingTop+width/2);
        Path path1 = new Path();
        Path path2 = new Path();

        path1.addCircle(0,0,radius1, Path.Direction.CCW);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path1,mPaint);

        if (isCheck){
            path2.addCircle(0,0,radius2, Path.Direction.CCW);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawPath(path2,mPaint);
        }
    }
}
