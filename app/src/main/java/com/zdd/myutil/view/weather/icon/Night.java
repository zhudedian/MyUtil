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
 * Created by yd on 2018/4/28.
 */

public class Night extends View {

    private Paint mPaint,starPaint1,starPaint2,starPaint3;
    private float radius1,radius2;
    private int paintWidth = 6;
    private int alpha1,alpha2,alpha3;
    private float angle = 0;
    private boolean isStartedAnimate = false;
    private float width,height;
    private float paddingLeft,paddingRight,paddingTop,paddingBottom;
    public Night(Context context, int paintWidth) {

        this(context, null);
        this.paintWidth = paintWidth;
        mPaint.setStrokeWidth(paintWidth);
    }
    public Night(Context context) {
        this(context, null);
    }

    public Night(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Night(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(255);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(paintWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStrokeJoin(Paint.Join.MITER);//锐角
        mPaint.setStrokeJoin(Paint.Join.ROUND);//圆弧

        starPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        starPaint1.setColor(Color.WHITE);
        starPaint1.setAlpha(alpha1);
        starPaint1.setStyle(Paint.Style.STROKE);
        starPaint1.setStrokeWidth(4);
        starPaint1.setStrokeCap(Paint.Cap.ROUND);
//        starPaint1.setStrokeJoin(Paint.Join.MITER);//锐角
        starPaint1.setStrokeJoin(Paint.Join.ROUND);//圆弧

        starPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        starPaint2.setColor(Color.WHITE);
        starPaint2.setAlpha(alpha1-30);
        starPaint2.setStyle(Paint.Style.STROKE);
        starPaint2.setStrokeWidth(3.2f);
        starPaint2.setStrokeCap(Paint.Cap.ROUND);
//        starPaint2.setStrokeJoin(Paint.Join.MITER);//锐角
        starPaint2.setStrokeJoin(Paint.Join.ROUND);//圆弧

        starPaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        starPaint3.setColor(Color.WHITE);
        starPaint3.setAlpha(alpha1-90);
        starPaint3.setStyle(Paint.Style.STROKE);
        starPaint3.setStrokeWidth(2.0f);
        starPaint3.setStrokeCap(Paint.Cap.ROUND);
//        starPaint2.setStrokeJoin(Paint.Join.MITER);//锐角
        starPaint3.setStrokeJoin(Paint.Join.ROUND);//圆弧

    }
    private void init() {





        paddingLeft = getPaddingLeft()+paintWidth/2;
        paddingRight = getPaddingRight()+paintWidth/2;
        paddingTop = getPaddingTop()+paintWidth/2;
        paddingBottom = getPaddingBottom()+paintWidth/2;

        width = getWidth()-paddingLeft-paddingRight;
        height = getHeight()-paddingTop-paddingBottom;

        radius1 = width*45/100;
        radius2 = radius1;

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        canvas.translate(paddingLeft, paddingTop);

        Path path1 = new Path();
        Path path2 = new Path();

        path1.addCircle(radius1*90/100,radius1*93/100,radius1, Path.Direction.CCW);
        path2.addCircle(radius2*36/100,radius2*43/100,radius2, Path.Direction.CCW);
        path1.op(path2, Path.Op.DIFFERENCE);
        canvas.drawPath(path1,mPaint);

        drawStar(canvas,alpha1,width*0.35f,0,width*0.11f);
        drawStar(canvas,alpha2,width*0.22f,width*0.40f,width*0.08f);
        drawStar(canvas,alpha3,width*0.90f,width*0.75f,width*0.08f);

        startAnimator();
    }

    private void drawStar(Canvas canvas, int alpha, float x, float y, float h){
        starPaint1.setAlpha(alpha);
        int alpha2 = (int) (alpha*0.95);
        int alpha3 = (int) (alpha*0.7);
        starPaint2.setAlpha(alpha2);
        starPaint3.setAlpha(alpha3);
        float starX = x;
        float starY = y;
        float starH = h;
        Path starPath1 = new Path();
        starPath1.moveTo(starX,starY+starH*0.5f);
        starPath1.lineTo(starX+starH*0.4f,starY+starH*0.5f);
        starPath1.moveTo(starX+starH*0.2f,starY+starH*0.3f);
        starPath1.lineTo(starX+starH*0.2f,starY+starH*0.7f);
        canvas.drawPath(starPath1,starPaint1);
        starPath1 = new Path();
        starPath1.moveTo(starX,starY+starH*0.52f);
        starPath1.lineTo(starX+starH*0.48f,starY+starH*0.52f);
        starPath1.moveTo(starX+starH*0.21f,starY+starH*0.15f);
        starPath1.lineTo(starX+starH*0.21f,starY+starH*0.75f);
        canvas.drawPath(starPath1,starPaint2);
        starPath1 = new Path();
        starPath1.moveTo(starX,starY+starH*0.55f);
        starPath1.lineTo(starX+starH*0.6f,starY+starH*0.55f);
        starPath1.moveTo(starX+starH*0.25f,starY);
        starPath1.lineTo(starX+starH*0.25f,starY+starH*0.9f);
        canvas.drawPath(starPath1,starPaint3);
    }
    private void startAnimator() {
        if (isStartedAnimate){
            return;
        }

        isStartedAnimate = true;
        final ValueAnimator animator = ValueAnimator.ofInt(0, 510);
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
                alpha1 =  (int)animation.getAnimatedValue();
                alpha2 = alpha1-80;
                alpha3 = alpha1-150;
                if (alpha1>255){
                    alpha1=510-alpha1;
                }else if (alpha1<0){
                    alpha1*=-1;
                }
                if (alpha2>255){
                    alpha2=510-alpha2;
                }else if (alpha2<0){
                    alpha2*=-1;
                }
                if (alpha3>255){
                    alpha3=510-alpha3;
                }else if (alpha3<0){
                    alpha3*=-1;
                }

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
