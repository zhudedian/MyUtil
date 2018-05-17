package com.zdd.myutil.view.weather.icon;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by yd on 2018/4/23.
 */

public class Sun extends View {
    private Paint mPaint;
    private int radius;
    private int paintWidth = 6;
    private float angle = 0;
    private boolean isStartedAnimate = false;
    private int width,height;
    private int paddingLeft,paddingRight,paddingTop,paddingBottom;
    public Sun(Context context, int paintWidth) {

        this(context, null);
        this.paintWidth = paintWidth;
        mPaint.setStrokeWidth(paintWidth);
    }
    public Sun(Context context) {
        this(context, null);
    }

    public Sun(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Sun(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(255);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(paintWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//没有
        mPaint.setStrokeJoin(Paint.Join.BEVEL);//直线

    }
    private void init() {





        paddingLeft = getPaddingLeft()+paintWidth/2;
        paddingRight = getPaddingRight()+paintWidth/2;
        paddingTop = getPaddingTop()+paintWidth/2;
        paddingBottom = getPaddingBottom()+paintWidth/2;

        width = getWidth()-paddingLeft-paddingRight;
        height = getHeight()-paddingTop-paddingBottom;

        radius = width/2;

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        canvas.translate(paddingLeft+width/2, paddingTop+width/2);

        Path path = new Path();
        path.addCircle(0,0,radius*55/100, Path.Direction.CCW);
        canvas.drawPath(path,mPaint);
        path = new Path();
        int count = 12;
        for (int i=0;i<count;i++){
            path.moveTo(radius*cos(360/count*i)*72/100,radius*sin(360/count*i)*72/100);//绘制起点
            path.lineTo(radius*cos(360/count*i),radius*sin(360/count*i));

        }
        canvas.drawPath(path,mPaint);
        startAnimator();
    }
    private void startAnimator() {
        if (isStartedAnimate){
            return;
        }

        isStartedAnimate = true;
        final ValueAnimator animator = ValueAnimator.ofFloat(0, 30);
        animator.setDuration(3000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 通过这样一个监听事件，我们就可以获取
                 * 到ValueAnimator每一步所产生的值。
                 *
                 * 通过调用getAnimatedValue()获取到每个时间因子所产生的Value。
                 * */
                angle =  (float)animation.getAnimatedValue();
//                Log.i("edong","angle=" +angle);
                postInvalidate();
            }
        });
        animator.start();
    }
    float sin(int num){
        return (float) Math.sin((num+angle)* Math.PI/180);
    }
    float cos(int num){
        return (float) Math.cos((num+angle)* Math.PI/180);
    }
}
