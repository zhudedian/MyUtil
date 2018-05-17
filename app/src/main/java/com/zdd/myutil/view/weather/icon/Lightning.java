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
 * Created by yd on 2018/4/25.
 */

public class Lightning extends View {
    private Paint mPaint,mPaint2;
    private int color = Color.WHITE;
    private float radius1, radius2,radius3,radius4,radius5;
    private int paintWidth = 5;
    private float angle = 0;
    private boolean isStartedAnimate = false;
    private float breakp1,breakp2;
    private float breakW = 10;
    private int width,width1,height,height1;
    private int paddingLeft,paddingRight,paddingTop,paddingBottom;

    public Lightning(Context context, int paintWidth) {

        this(context, null);
        this.paintWidth = paintWidth;
        mPaint.setStrokeWidth(paintWidth);
    }

    public Lightning(Context context) {
        this(context, null);
    }

    public Lightning(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Lightning(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint(){
        if (mPaint!=null){
            return;
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setAlpha(255);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(paintWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//没有
        mPaint.setStrokeJoin(Paint.Join.ROUND);//直线

        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setColor(color);
        mPaint2.setAlpha(255);

        mPaint2.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint2.setStrokeWidth(paintWidth/2);
        mPaint2.setStrokeCap(Paint.Cap.ROUND);//没有
        mPaint2.setStrokeJoin(Paint.Join.ROUND);//直线
    }

    private void init() {
//        initPaint();

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


        float cloudX = width-width1;
        float cloudY = 0;
        path1.addCircle(radius1+cloudX,height1-radius1+cloudY,radius1, Path.Direction.CCW);
        path2.addCircle(width1*28/100+cloudX,height1-width1*21/100+cloudY,radius2, Path.Direction.CCW);
        path3.addCircle(width1*48/100+cloudX,height1-width1*33/100+cloudY,radius3, Path.Direction.CCW);
        path4.addCircle(width1-radius4+cloudX,height1-radius4+cloudY,radius4, Path.Direction.CCW);
        path5.addRect(radius1+cloudX, height1-radius1*2+cloudY, width1 -radius4+cloudX, height1+cloudY, Path.Direction.CCW);

        path1.op(path2, Path.Op.UNION);
        path1.op(path3, Path.Op.UNION);
        path1.op(path4, Path.Op.UNION);
        path1.op(path5, Path.Op.UNION);

        Path path11 = new Path();
        Path path22 = new Path();
        Path path33 = new Path();
        Path path44 = new Path();
        Path path55 = new Path();

        float cloudX1 = 0;
        float cloudY1 = -radius3;
        float scale = 75.0f/100;
        float width2 = width1*scale;
        float height2 = height1;
        float radius11 = radius1*scale;
        float radius22 = radius2*scale;
        float radius33 = radius3*scale;
        float radius44 = radius4*scale;

        path11.addCircle(radius11+cloudX1,height2-radius11+cloudY1,radius11, Path.Direction.CCW);
        path22.addCircle(width2*28/100+cloudX1,height2-width2*21/100+cloudY1,radius22, Path.Direction.CCW);
        path33.addCircle(width2*48/100+cloudX1,height2-width2*33/100+cloudY1,radius33, Path.Direction.CCW);
        path44.addCircle(width2-radius44+cloudX1,height2-radius44+cloudY1,radius44, Path.Direction.CCW);
        path55.addRect(radius11+cloudX1, height2-radius11*2+cloudY1, width2 -radius44+cloudX1, height2+cloudY1, Path.Direction.CCW);







        path11.op(path22, Path.Op.UNION);
        path11.op(path33, Path.Op.UNION);
        path11.op(path44, Path.Op.UNION);
        path11.op(path55, Path.Op.UNION);

        path11.op(path1, Path.Op.DIFFERENCE);


        canvas.drawPath(path11,mPaint);
        canvas.save();
        Path clipPath = new Path();
        clipPath.moveTo(-paddingLeft,-paddingTop);
        clipPath.lineTo(width+paddingRight,-paddingTop);
        clipPath.lineTo(width+paddingRight,height1+cloudY+paintWidth);
        clipPath.lineTo(width-width1*36/100,height1+cloudY+paintWidth);
        clipPath.lineTo(width-width1*36/100,height1+cloudY+paintWidth-radius1);
        clipPath.lineTo(width-width1*53/100,height1+cloudY+paintWidth-radius1);
        clipPath.lineTo(width-width1*53/100,height1+cloudY+paintWidth);
        clipPath.lineTo(-paddingLeft,height1+cloudY+paintWidth);
        clipPath.close();
        canvas.clipPath(clipPath);
        canvas.drawPath(path1,mPaint);
        canvas.restore();

//        canvas.drawCircle(width-width1*35/100,height1+cloudY,paintWidth/2,mPaint2);
//        canvas.drawCircle(width-width1*55/100,height1+cloudY,paintWidth/2,mPaint2);

        drawLightnings(canvas);
        startAnimator();
    }

    private void drawLightnings(Canvas canvas){
        Path pathR = new Path();
        float lightA = 0.6f;

        float lightX1 = width-width1*62/100;
        float lightH1 = width1*0.25f;
        float lightY1 = height1+lightH1*0.4f;

        float lightX2 = width-width1*39/100;
        float lightH2 = width1*0.40f;
        float lightY2 = height1-lightH2*20/100;

        //第一个闪电
        if (breakp1<=breakW) {

        }else if (breakp1>breakW&&breakp1<=lightH1/2+breakW){
            pathR.moveTo(lightX1, lightY1);
            pathR.lineTo(lightX1 - (breakp1-breakW) * lightA, lightY1 + breakp1-breakW);
        }else if (breakp1>lightH1/2+breakW&&breakp1<=lightH1/2+lightH1/2 * lightA+breakW){
            pathR.moveTo(lightX1, lightY1);
            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1/2);
            pathR.lineTo(lightX1 - lightH1/2 * lightA+breakp1-breakW-lightH1/2, lightY1 + lightH1/2);
        }else if (breakp1>lightH1/2+lightH1/2 * lightA+breakW&&breakp1<lightH1+lightH1/2 * lightA+breakW){
            pathR.moveTo(lightX1, lightY1);
            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1/2);
            pathR.lineTo(lightX1, lightY1 + lightH1/2);
            pathR.lineTo(lightX1 - lightH1/2 * lightA-(breakp1-lightH1-lightH1/2 * lightA-breakW)*lightA, lightY1 + breakp1-lightH1/2 * lightA-breakW);
        }else {
            pathR.moveTo(lightX1, lightY1);
            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1/2);
            pathR.lineTo(lightX1, lightY1 + lightH1/2);
            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1);
        }
        //第二个闪电
        if (breakp2<=breakW){

        }else if (breakp2>breakW&&breakp2<=lightH2/2+breakW){
            pathR.moveTo(lightX2, lightY2);
            pathR.lineTo(lightX2 - (breakp2-breakW) * lightA, lightY2 + breakp2-breakW);
        }else if (breakp2>lightH2/2+breakW&&breakp2<=lightH2/2+lightH2/2 * lightA+breakW){
            pathR.moveTo(lightX2, lightY2);
            pathR.lineTo(lightX2 - lightH2/2 * lightA, lightY2 + lightH2/2);
            pathR.lineTo(lightX2 - lightH2/2 * lightA+breakp2-breakW-lightH2/2, lightY2 + lightH2/2);
        }else if (breakp2>lightH2/2+lightH2/2 * lightA+breakW&&breakp2<lightH2+lightH2/2 * lightA+breakW){
            pathR.moveTo(lightX2, lightY2);
            pathR.lineTo(lightX2 - lightH2/2 * lightA, lightY2 + lightH2/2);
            pathR.lineTo(lightX2, lightY2 + lightH2/2);
            pathR.lineTo(lightX2 - lightH2/2 * lightA-(breakp2-lightH2-lightH2/2 * lightA-breakW)*lightA, lightY2 + breakp2-lightH2/2 * lightA-breakW);
        }else {
            pathR.moveTo(lightX2, lightY2);
            pathR.lineTo(lightX2 - lightH2/2 * lightA, lightY2 + lightH2/2);
            pathR.lineTo(lightX2, lightY2 + lightH2/2);
            pathR.lineTo(lightX2 - lightH2/2 * lightA, lightY2 + lightH2);
        }

//        if (breakp1<=-breakW) {
//            pathR.moveTo(lightX1, lightY1);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1);
//        }else if (breakp1<=breakW){
//            pathR.moveTo(lightX1-(breakp1+breakW)*lightA, lightY1+breakp1+breakW);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1);
//        }else if (breakp1>breakW&&breakp1<lightH1/2-breakW){
//            pathR.moveTo(lightX1, lightY1);
//            pathR.lineTo(lightX1 - (breakp1-breakW) * lightA, lightY1 + breakp1-breakW);
//            pathR.moveTo(lightX1 - (breakp1+breakW) * lightA, lightY1 + breakp1+breakW);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1);
//        }else if (breakp1>=lightH1/2-breakW&&breakp1<=lightH1/2+breakW){
//            pathR.moveTo(lightX1, lightY1);
//            pathR.lineTo(lightX1 - (breakp1-breakW) * lightA, lightY1 + breakp1-breakW);
//            pathR.moveTo(lightX1 - lightH1/2 * lightA+breakp1+breakW-lightH1/2, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1);
//        }else if (breakp1>lightH1/2+breakW&&breakp1<lightH1/2+lightH1/2 * lightA-breakW){
//            pathR.moveTo(lightX1, lightY1);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA+breakp1-breakW-lightH1/2, lightY1 + lightH1/2);
//            pathR.moveTo(lightX1 - lightH1/2 * lightA+breakp1+breakW-lightH1/2, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1);
//        }else if (breakp1>=lightH1/2+lightH1/2 * lightA-breakW&&breakp1<=lightH1/2+lightH1/2 * lightA+breakW){
//            pathR.moveTo(lightX1, lightY1);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA+breakp1-breakW-lightH1/2, lightY1 + lightH1/2);
//            pathR.moveTo(lightX1-(breakp1-lightH1/2-lightH1/2 * lightA+breakW)*lightA, lightY1 +breakp1-lightH1/2 * lightA+breakW);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1);
//        }else if (breakp1>lightH1/2+lightH1/2 * lightA+breakW&&breakp1<lightH1+lightH1/2 * lightA-breakW){
//            pathR.moveTo(lightX1, lightY1);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1-(breakp1-lightH1/2-lightH1/2 * lightA-breakW)*lightA, lightY1 +breakp1-lightH1/2 * lightA-breakW);
//            pathR.moveTo(lightX1-(breakp1-lightH1/2-lightH1/2 * lightA+breakW)*lightA, lightY1 +breakp1-lightH1/2 * lightA+breakW);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1);
//        }else if (breakp1>=lightH1+lightH1/2 * lightA-breakW&&breakp1<lightH1+lightH1/2 * lightA+breakW){
//            pathR.moveTo(lightX1, lightY1);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA-(breakp1-lightH1-lightH1/2 * lightA-breakW)*lightA, lightY1 + breakp1-lightH1/2 * lightA-breakW);
//        }else {
//            pathR.moveTo(lightX1, lightY1);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1, lightY1 + lightH1/2);
//            pathR.lineTo(lightX1 - lightH1/2 * lightA, lightY1 + lightH1);
//        }
        canvas.drawPath(pathR,mPaint);
    }

    private void startAnimator() {
        if (isStartedAnimate){
            return;
        }

        isStartedAnimate = true;
        final ValueAnimator animator = ValueAnimator.ofFloat(-20, 500);
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
                breakp2 =  (float)animation.getAnimatedValue();
                breakp1 = breakp2 - 110;
//                Log.i("edong","angle=" +angle);
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
