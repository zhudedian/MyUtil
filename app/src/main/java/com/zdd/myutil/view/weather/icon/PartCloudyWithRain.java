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
 * Created by yd on 2018/5/8.
 */

public class PartCloudyWithRain extends View {

    private Paint mPaint;
    private float radius1, radius2,radius3,radius4,radius5;
    private int paintWidth = 5;
    private float angle = 0;
    private boolean isStartedAnimate = false;
    private float breakp1,breakp2,breakp3,breakp4,breakp5;
    private float breakW = 7;
    private int width,width1,height,height1;
    private int paddingLeft,paddingRight,paddingTop,paddingBottom;
    public PartCloudyWithRain(Context context, int paintWidth) {

        this(context, null);
        this.paintWidth = paintWidth;
        mPaint.setStrokeWidth(paintWidth);
    }
    public PartCloudyWithRain(Context context) {
        this(context, null);
    }

    public PartCloudyWithRain(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PartCloudyWithRain(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(255);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(paintWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//没有
        mPaint.setStrokeJoin(Paint.Join.ROUND);//直线

    }
    private void init() {





        paddingLeft = getPaddingLeft()+paintWidth/2;
        paddingRight = getPaddingRight()+paintWidth/2;
        paddingTop = getPaddingTop()+paintWidth/2;
        paddingBottom = getPaddingBottom()+paintWidth/2;

        width = getWidth()-paddingLeft-paddingRight;
        height = getHeight()-paddingTop-paddingBottom;

        width1 = width*90/100;
        height1 = width*66/100;

        radius1 = width1*14/100;
        radius2 = width1*17/100;
        radius3 = width1*24/100;
        radius4 = width1*21/100;
        radius5 = width*32/100;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        canvas.translate(paddingLeft, paddingTop);
        Path path1 = new Path();
        Path path2 = new Path();
        Path path3 = new Path();
        Path path4 = new Path();
        Path path5 = new Path();
        Path path6 = new Path();
        Path path7 = new Path();

        float cloudX = width-width1;
        float cloudY = 0;
        path1.addCircle(radius1+cloudX,height1-radius1+cloudY,radius1, Path.Direction.CCW);
        path2.addCircle(width1*28/100+cloudX,height1-width1*21/100+cloudY,radius2, Path.Direction.CCW);
        path3.addCircle(width1*48/100+cloudX,height1-width1*33/100+cloudY,radius3, Path.Direction.CCW);
        path4.addCircle(width1-radius4+cloudX,height1-radius4+cloudY,radius4, Path.Direction.CCW);
        path5.addRect(radius1+cloudX, height1-radius1*2+cloudY, width1 -radius4+cloudX, height1+cloudY, Path.Direction.CCW);

        float circle5X = radius5;
        float circle5Y = height1-radius5*105/100;
        path6.addCircle(circle5X,circle5Y,radius5*62/100, Path.Direction.CCW);



        path1.op(path2, Path.Op.UNION);
        path1.op(path3, Path.Op.UNION);
        path1.op(path4, Path.Op.UNION);
        path1.op(path5, Path.Op.UNION);

        path6.op(path1, Path.Op.DIFFERENCE);

        canvas.drawPath(path1,mPaint);
        canvas.drawPath(path6,mPaint);
        int count = 12;
        for (int i=0;i<count;i++){
            path7 = new Path();
            path7.moveTo(radius5*cos(360.0f/count*i)*77/100+circle5X,circle5Y+radius5*sin(360.0f/count*i)*77/100);//绘制起点
            path7.lineTo(radius5*cos(360.0f/count*i)+circle5X,circle5Y+radius5*sin(360.0f/count*i));
            path7.lineTo(radius5*cos(360.0f/count*i+0.1f)+circle5X,circle5Y+radius5*sin(360.0f/count*i+0.1f));
            path7.close();
            path7.op(path1, Path.Op.DIFFERENCE);
            canvas.drawPath(path7,mPaint);
        }
        drawRains(canvas);
        startAnimator();
    }
    private void drawRains(Canvas canvas){
        Path pathR = new Path();
        float rainsA = 0.6f;
        float rainsSpan = 28;

        float rainsX1 = width-width1/2-rainsSpan*2;
        float rainsY1 = height1+10;
        float rainsH1 = width1*0.16f;

        float rainsX2 = width-width1/2-rainsSpan;
        float rainsY2 = height1+10;
        float rainsH2 = width1*0.20f;

        float rainsX3 = width-width1/2;
        float rainsY3 = height1+10;
        float rainsH3 = width1*0.16f;

        float rainsX4 = width-width1/2+rainsSpan;
        float rainsY4 = height1+10;
        float rainsH4 = width1*0.25f;

        float rainsX5 = width-width1/2+rainsSpan*2;
        float rainsY5 = height1+10;
        float rainsH5 = width1*0.20f;



        //第一滴水
        if (breakp1<=-breakW) {
            pathR.moveTo(rainsX1, rainsY1);
            pathR.lineTo(rainsX1 - rainsH1 * rainsA, rainsY1 + rainsH1);
        }else if (breakp1<=breakW){
            pathR.moveTo(rainsX1-(breakp1+breakW)*rainsA, rainsY1+breakp1+breakW);
            pathR.lineTo(rainsX1 - rainsH1 * rainsA, rainsY1 + rainsH1);
        }else if (breakp1>breakW&&breakp1<rainsH1-breakW){
            pathR.moveTo(rainsX1, rainsY1);
            pathR.lineTo(rainsX1 - (breakp1-breakW) * rainsA, rainsY1 + breakp1-breakW);
            pathR.moveTo(rainsX1 - (breakp1+breakW) * rainsA, rainsY1 + breakp1+breakW);
            pathR.lineTo(rainsX1 - rainsH1 * rainsA, rainsY1 + rainsH1);
        }else if (breakp1>=rainsH1-breakW&&breakp1<=rainsH1+breakW){
            pathR.moveTo(rainsX1, rainsY1);
            pathR.lineTo(rainsX1 - (breakp1-breakW) * rainsA, rainsY1 + breakp1-breakW);
        }else {
            pathR.moveTo(rainsX1, rainsY1);
            pathR.lineTo(rainsX1 - rainsH1 * rainsA, rainsY1 + rainsH1);
        }

        //第二滴水
        if (breakp2<=-breakW) {
            pathR.moveTo(rainsX2, rainsY2);
            pathR.lineTo(rainsX2 - rainsH2 * rainsA, rainsY2 + rainsH2);
        }else if (breakp2<=breakW){
            pathR.moveTo(rainsX2-(breakp2+breakW)*rainsA, rainsY2+breakp2+breakW);
            pathR.lineTo(rainsX2 - rainsH2 * rainsA, rainsY2 + rainsH2);
        }else if (breakp2>breakW&&breakp2<rainsH2-breakW){
            pathR.moveTo(rainsX2, rainsY2);
            pathR.lineTo(rainsX2 - (breakp2-breakW) * rainsA, rainsY2 + breakp2-breakW);
            pathR.moveTo(rainsX2 - (breakp2+breakW) * rainsA, rainsY2 + breakp2+breakW);
            pathR.lineTo(rainsX2 - rainsH2 * rainsA, rainsY2 + rainsH2);
        }else if (breakp2>=rainsH2-breakW&&breakp2<=rainsH2+breakW){
            pathR.moveTo(rainsX2, rainsY2);
            pathR.lineTo(rainsX2 - (breakp2-breakW) * rainsA, rainsY2 + breakp2-breakW);
        }else {
            pathR.moveTo(rainsX2, rainsY2);
            pathR.lineTo(rainsX2 - rainsH2 * rainsA, rainsY2 + rainsH2);
        }

        //第三滴水
        if (breakp3<=-breakW) {
            pathR.moveTo(rainsX3, rainsY3);
            pathR.lineTo(rainsX3 - rainsH3 * rainsA, rainsY3 + rainsH3);
        }else if (breakp3<=breakW){
            pathR.moveTo(rainsX3-(breakp3+breakW)*rainsA, rainsY3+breakp3+breakW);
            pathR.lineTo(rainsX3 - rainsH3 * rainsA, rainsY1 + rainsH3);
        }else if (breakp3>breakW&&breakp3<rainsH3-breakW){
            pathR.moveTo(rainsX3, rainsY3);
            pathR.lineTo(rainsX3 - (breakp3-breakW) * rainsA, rainsY3 + breakp3-breakW);
            pathR.moveTo(rainsX3 - (breakp3+breakW) * rainsA, rainsY3 + breakp3+breakW);
            pathR.lineTo(rainsX3 - rainsH3 * rainsA, rainsY3 + rainsH3);
        }else if (breakp3>=rainsH3-breakW&&breakp3<=rainsH3+breakW){
            pathR.moveTo(rainsX3, rainsY3);
            pathR.lineTo(rainsX3 - (breakp3-breakW) * rainsA, rainsY3 + breakp3-breakW);
        }else {
            pathR.moveTo(rainsX3, rainsY3);
            pathR.lineTo(rainsX3 - rainsH3 * rainsA, rainsY3 + rainsH3);
        }

        //第四滴水
        if (breakp4<=-breakW) {
            pathR.moveTo(rainsX4, rainsY4);
            pathR.lineTo(rainsX4 - rainsH4 * rainsA, rainsY4 + rainsH4);
        }else if (breakp4<=breakW){
            pathR.moveTo(rainsX4-(breakp4+breakW)*rainsA, rainsY4+breakp4+breakW);
            pathR.lineTo(rainsX4 - rainsH4 * rainsA, rainsY4 + rainsH4);
        }else if (breakp4>breakW&&breakp4<rainsH4-breakW){
            pathR.moveTo(rainsX4, rainsY4);
            pathR.lineTo(rainsX4 - (breakp4-breakW) * rainsA, rainsY4 + breakp4-breakW);
            pathR.moveTo(rainsX4 - (breakp4+breakW) * rainsA, rainsY4 + breakp4+breakW);
            pathR.lineTo(rainsX4 - rainsH4 * rainsA, rainsY4 + rainsH4);
        }else if (breakp4>=rainsH4-breakW&&breakp4<=rainsH4+breakW){
            pathR.moveTo(rainsX4, rainsY4);
            pathR.lineTo(rainsX4 - (breakp4-breakW) * rainsA, rainsY4 + breakp4-breakW);
        }else {
            pathR.moveTo(rainsX4, rainsY4);
            pathR.lineTo(rainsX4 - rainsH4 * rainsA, rainsY4 + rainsH4);
        }
        //第五滴水
        if (breakp5<=-breakW) {
            pathR.moveTo(rainsX5, rainsY5);
            pathR.lineTo(rainsX5 - rainsH5 * rainsA, rainsY5 + rainsH5);
        }else if (breakp5<=breakW){
            pathR.moveTo(rainsX5-(breakp5+breakW)*rainsA, rainsY5+breakp5+breakW);
            pathR.lineTo(rainsX5 - rainsH5 * rainsA, rainsY5 + rainsH5);
        }else if (breakp5>breakW&&breakp5<rainsH5-breakW){
            pathR.moveTo(rainsX5, rainsY5);
            pathR.lineTo(rainsX5 - (breakp5-breakW) * rainsA, rainsY5 + breakp5-breakW);
            pathR.moveTo(rainsX5 - (breakp5+breakW) * rainsA, rainsY5 + breakp5+breakW);
            pathR.lineTo(rainsX5 - rainsH5 * rainsA, rainsY5 + rainsH5);
        }else if (breakp5>=rainsH5-breakW&&breakp5<=rainsH5+breakW){
            pathR.moveTo(rainsX5, rainsY5);
            pathR.lineTo(rainsX5 - (breakp5-breakW) * rainsA, rainsY5 + breakp5-breakW);
        }else {
            pathR.moveTo(rainsX5, rainsY5);
            pathR.lineTo(rainsX5 - rainsH5 * rainsA, rainsY5 + rainsH5);
        }
        canvas.drawPath(pathR,mPaint);
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
                breakp1 = angle*220/30-20;
                breakp2 = breakp1 - 80;
                breakp3 = breakp1 - 40;
                breakp4 = breakp1 - 110;
                breakp5 = breakp1 - 50;
                postInvalidate();
            }
        });
        animator.start();
    }
    float sin(float num){
        return (float) Math.sin((num+angle)* Math.PI/180);
    }
    float cos(float num){
        return (float) Math.cos((num+angle)* Math.PI/180);
    }
}
