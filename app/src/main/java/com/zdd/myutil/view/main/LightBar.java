package com.zdd.myutil.view.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zdd.myutil.R;


/**
 * Created by yd on 2018/5/4.
 */

public class LightBar extends View {
    private Paint mPaint;
    private float lp = 0.25f;
    private float lw = 0.2f;
    private float law = 0.2f;
    private int color = getResources().getColor(R.color.lightbar_mid);
    private int paintWidth = 5;
    private Type type = Type.SPEAKING;
    private ValueAnimator animator;
    private boolean isStartedAnimate = false;

    public LightBar(Context context) {
        this(context, null);
    }

    public LightBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LightBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(255);

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(paintWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//没有
        mPaint.setStrokeJoin(Paint.Join.BEVEL);//直线

    }


    public enum Type{
        LISTENING,ACTIVELISTENING,THINKING,SPEAKING,MICROPHONEOFF,SYSTEMERROR
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (type== Type.LISTENING) {
            drawListening(canvas);
        }else if (type == Type.ACTIVELISTENING) {
            drawActiveListening(canvas);
        }else if (type == Type.THINKING) {
            drawThinking(canvas);
        }else if (type == Type.SPEAKING) {
            drawSpeaking(canvas);
        }else if (type == Type.MICROPHONEOFF) {
            drawMicrophoneOff(canvas);
        }else {
            drawSystemError(canvas);
        }
    }

    public void show(Type type){
        this.type = type;
        handler.removeCallbacks(setGone);
        setVisibility(VISIBLE);
        startAnimate();
//        handler.postDelayed(setGone,10000);
    }

    public void dismiss(){
        setVisibility(GONE);
    }

    private void startAnimate(){
        if (type== Type.LISTENING) {
            startListeningAnimator();
        }else if (type == Type.ACTIVELISTENING) {
            startActiveListeningAnimator();
        }else if (type == Type.THINKING) {
            startThinkingAnimator();
            handler.postDelayed(setGone,5000);
        }else if (type == Type.SPEAKING) {
            startSpeakingAnimator();
        }else {
            postInvalidate();
            handler.postDelayed(setGone,10000);
        }
    }
    private Handler handler = new Handler(Looper.getMainLooper());
    Runnable setGone = new Runnable() {
        @Override
        public void run() {
            setVisibility(GONE);
        }
    };
    private void drawMicrophoneOff(Canvas canvas){
        LinearGradient linearGradient;
        lp = 1;
        linearGradient = new LinearGradient(0, 0, getMeasuredWidth() * 0.5f, 0,
                new int[]{Color.RED, Color.RED},
                null, LinearGradient.TileMode.MIRROR);

        mPaint.setShader(linearGradient);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

    }

    private void drawSystemError(Canvas canvas){
        LinearGradient linearGradient;
        lp = 1;
        linearGradient = new LinearGradient(0, 0, getMeasuredWidth() * 0.5f, 0,
                new int[]{getResources().getColor(R.color.orange),
                        getResources().getColor(R.color.orange)},
                null, LinearGradient.TileMode.MIRROR);

        mPaint.setShader(linearGradient);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

    }

    private void drawListening(Canvas canvas){
        LinearGradient linearGradient;
        if (lp<=lw/2) {
            linearGradient = new LinearGradient(0, 0, getMeasuredWidth() * 0.5f, 0,
                    new int[]{
                            getResources().getColor(R.color.lightbar_mid),
                            getResources().getColor(R.color.lightbar_mid), Color.BLUE, Color.BLUE},
                    new float[]{0f, lp+lw/2, lp+lw/2+law, 1f,}, LinearGradient.TileMode.MIRROR);
        }else if (lp>lw/2&&lp<=lw/2+law) {
            linearGradient = new LinearGradient(0, 0, getMeasuredWidth() * 0.5f, 0,
                    new int[]{Color.BLUE,
                            getResources().getColor(R.color.lightbar_mid),
                            getResources().getColor(R.color.lightbar_mid), Color.BLUE, Color.BLUE},
                    new float[]{0f, lp-lw/2, lp+lw/2, lp+lw/2+law, 1,}, LinearGradient.TileMode.MIRROR);
        }else if (lp>lw/2+law&&lp<1-lw/2-law) {
            linearGradient = new LinearGradient(0, 0, getMeasuredWidth() * 0.5f, 0,
                    new int[]{Color.BLUE, Color.BLUE,
                            getResources().getColor(R.color.lightbar_mid),
                            getResources().getColor(R.color.lightbar_mid), Color.BLUE, Color.BLUE},
                    new float[]{0f, lp-lw/2-law,lp-lw/2, lp+lw/2, lp+lw/2+law, 1,}, LinearGradient.TileMode.MIRROR);
        }else if (lp>=1-lw/2-law&&lp<=1-lw/2-law/2) {
            linearGradient = new LinearGradient(0, 0, getMeasuredWidth() * 0.5f, 0,
                    new int[]{Color.BLUE, Color.BLUE,
                            getResources().getColor(R.color.lightbar_mid),
                            getResources().getColor(R.color.lightbar_mid),  Color.BLUE},
                    new float[]{0f, lp-lw/2-law,lp-lw/2, lp+lw/2,  1f,}, LinearGradient.TileMode.MIRROR);
        }else{
            linearGradient = new LinearGradient(0, 0, getMeasuredWidth() * 0.5f, 0,
                    new int[]{Color.BLUE, Color.BLUE,
                            getResources().getColor(R.color.lightbar_mid),
                            getResources().getColor(R.color.lightbar_mid)},
                    new float[]{0f, lp-lw/2-law,lp-lw/2, 1f,}, LinearGradient.TileMode.MIRROR);
        }
        mPaint.setShader(linearGradient);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);



    }



    private void drawSpeaking(Canvas canvas){
        LinearGradient linearGradient;
        lp = 1;
        linearGradient = new LinearGradient(0, 0, getMeasuredWidth() * 0.5f, 0,
                new int[]{color,color},
                null, LinearGradient.TileMode.MIRROR);

        mPaint.setShader(linearGradient);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);


    }
    private void startSpeakingAnimator() {
        if (animator!=null){
            animator.cancel();
        }
        animator = ValueAnimator.ofObject(new ArgbEvaluator(),
                getResources().getColor(R.color.lightbar_mid), Color.BLUE,
                getResources().getColor(R.color.lightbar_mid));
        animator.setDuration(1000);
        animator.setRepeatCount(3);
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
                color =  (int)animation.getAnimatedValue();
//                Log.i("edong","angle=" +angle);
                postInvalidate();
            }
        });
        animator.start();
    }

    private void drawThinking(Canvas canvas){
        LinearGradient linearGradient;
        lp = 1;
        linearGradient = new LinearGradient(0, 0, getMeasuredWidth() * 0.5f, 0,
                new int[]{Color.BLUE, Color.BLUE,
                        getResources().getColor(R.color.lightbar_mid),
                        getResources().getColor(R.color.lightbar_mid)},
                new float[]{0f, lp-lw/2-law,lp-lw/2, 1f,}, LinearGradient.TileMode.MIRROR);

        mPaint.setShader(linearGradient);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);


    }
    private void startThinkingAnimator() {
        if (animator!=null){
            animator.cancel();
        }
        animator = ValueAnimator.ofFloat(lw, 2);
        animator.setDuration(600);
        animator.setRepeatCount(3);
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
                lw =  (float)animation.getAnimatedValue();
//                Log.i("edong","angle=" +angle);
                postInvalidate();
            }
        });
        animator.start();
    }

    private void drawActiveListening(Canvas canvas){
        LinearGradient linearGradient;
        lp = 1;
        linearGradient = new LinearGradient(0, 0, getMeasuredWidth() * 0.5f, 0,
                    new int[]{Color.BLUE, Color.BLUE,
                            getResources().getColor(R.color.lightbar_mid),
                            getResources().getColor(R.color.lightbar_mid)},
                    new float[]{0f, lp-lw/2-law,lp-lw/2, 1f,}, LinearGradient.TileMode.MIRROR);

        mPaint.setShader(linearGradient);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);


    }
    private void startActiveListeningAnimator() {
        if (isStartedAnimate){
            return;
        }
        isStartedAnimate = true;
        if (animator!=null){
            animator.cancel();
        }
        animator = ValueAnimator.ofFloat(lw, lw+0.2f,lw);
        animator.setDuration(600);
        animator.setRepeatCount(3);
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
                lw =  (float)animation.getAnimatedValue();
//                Log.i("edong","angle=" +angle);
                postInvalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isStartedAnimate = false;
            }
        });
        animator.start();
    }
    private void startListeningAnimator() {
        lw = 0.2f;
        if (animator!=null){
            animator.cancel();
        }
        animator = ValueAnimator.ofFloat(0, 1.0f);
        animator.setDuration(600);
//        animator.setRepeatCount(ValueAnimator.INFINITE);
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
                lp =  (float)animation.getAnimatedValue();
//                Log.i("edong","angle=" +angle);
                postInvalidate();
            }
        });

        animator.start();
    }
}
