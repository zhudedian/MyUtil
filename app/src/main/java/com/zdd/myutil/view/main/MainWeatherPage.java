package com.zdd.myutil.view.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zdd.myutil.view.weather.icon.Cloudy;
import com.zdd.myutil.view.weather.icon.Lightning;
import com.zdd.myutil.view.weather.icon.Night;
import com.zdd.myutil.view.weather.icon.PartCloudy;
import com.zdd.myutil.view.weather.icon.PartCloudyNight;
import com.zdd.myutil.view.weather.icon.PartCloudyWithRain;
import com.zdd.myutil.view.weather.icon.Rain;
import com.zdd.myutil.view.weather.icon.Sun;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yd on 2018/5/10.
 */

public class MainWeatherPage extends ViewGroup {

    private Paint mPaint;
    private int textSize = 80;
    private int paintWidth = 3;
    private int width,height;
    private int leftSpan;
    private int paddingLeft,paddingRight,paddingTop,paddingBottom;

    private String curTemp = "68°";
    private String viewType = "Partly cloudy";

    private TryText tryText;
    private List<View> childViews = new ArrayList<>();
    private int tempTextWith;
    private Type type = Type.NOLMAL;

    public enum Type{
        MOVE,NOLMAL,AUTOIN,AUTOOUT
    }

    public MainWeatherPage(Context context) {
        this(context, null);
    }

    public MainWeatherPage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainWeatherPage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//新建画笔，无锯齿
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(255);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(textSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTypeface(Typeface.SERIF);

        initview();

    }
    public void setData(String curTemp, String viewType){
        Log.i("edong","viewType="+viewType);
        this.curTemp = curTemp;
        this.viewType = viewType;
        this.type = Type.NOLMAL;
        initview();
    }
    public void setAlpha(float alpha){
        for (View view:childViews){
            view.setAlpha(alpha);
        }
    }
    public void fresh(Type type){
        this.type = type;
        initview();
    }
    private void initview() {
        removeAllViews();
        TextView textView = new TextView(getContext());
        textView.setText(changeToC(curTemp));
        textView.setTextSize(textSize);
        textView.setTextColor(Color.WHITE);
        addView(textView);
        //根据viewType来判断是什么icon图片
        if (viewType.equals("Cloudy")) {//阴天
            final View view = new Cloudy(getContext(),paintWidth);
            addView(view);
        }else if (viewType.equals("Rainy")){//雨天
            final View view = new Rain(getContext(),paintWidth);
            addView(view);
        }else if (viewType.equals("Partly cloudy")){//多云
            final View view = new PartCloudy(getContext(),paintWidth);
            addView(view);
        }else if (viewType.equals("Sunny")){//太阳
            final View view = new Sun(getContext(),paintWidth);
            addView(view);
        }else if (viewType.equals("Lightning")){//雷电
            final View view = new Lightning(getContext(),paintWidth);
            addView(view);
        }else if (viewType.equals("Partly cloudy with rain")) {//多云转雨
            final View view = new PartCloudyWithRain(getContext(),paintWidth);
            addView(view);
        }else if (viewType.equals("Partly cloudy night")||viewType.equals("Nuit partiellement couverte")) {//晚上多云
            final View view = new PartCloudyNight(getContext(),paintWidth);
            addView(view);
        }else if (viewType.equals("Night")) {//晚上晴天
            final View view = new Night(getContext(),paintWidth);
            addView(view);
        }else if (viewType.equals("Clear night")) {//晚上晴天
            final View view = new Night(getContext());
            addView(view);
        }else {
            final View view = new PartCloudyNight(getContext(),paintWidth);
            addView(view);
        }
        if (tryText==null){
            tryText = new TryText(getContext());
            tryText.setTextSize(35);
            tryText.setTextColor(Color.WHITE);
        }
        if (type!= Type.AUTOOUT) {
            tryText.fresh();
        }
        addView(tryText);
        postInvalidate();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
        width = w-paddingLeft-paddingRight;//宽度是减去两边的padding
        height = h-paddingTop-paddingBottom;
        leftSpan = (int) (width*0.08f);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v.getVisibility() != View.GONE) {
                measureChild(v, widthMeasureSpec, heightMeasureSpec);

            }
        }

    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
//        canvas.drawText(changeToC(curTemp),leftSpan,height*18/100,mPaint);

        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        childViews.clear();
        int tempTextHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (i==0) {
                tempTextWith = childView.getMeasuredWidth();
                tempTextHeight = childView.getMeasuredHeight();
                childView.layout(leftSpan , 0,
                        leftSpan + tempTextWith, childView.getMeasuredHeight());
            }else if (i==1) {
                childView.layout(leftSpan + tempTextWith, 0,
                        leftSpan + tempTextWith+tempTextHeight, tempTextHeight);
            }else if (i==2){
                childView.layout(leftSpan , height-50,
                        leftSpan + childView.getMeasuredWidth(), height);
            }

            if (type== Type.MOVE) {
                startInAnimator(childView, i);
            }
            else if (type == Type.AUTOIN){
                startAutoInAnimator(childView,i);
            }else if (type == Type.AUTOOUT){
                startAutoOutAnimator(childView,i);
            }
            childViews.add(childView);
        }
    }
    public interface AutoOutListener{
        void animaEnd();
    }
    private AutoOutListener listener;
    public void setAutoOutListener(AutoOutListener listener){
        this.listener = listener;
    }
    private void startAutoOutAnimator(View view, int index){
        if (index==0) {
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0F);
            animator2.setDuration(800).start();
        }else if (index==1) {
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0F);
            animator2.setDuration(800).start();
        }else if (index==2) {
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0F);
            animator2.setDuration(800).start();
            animator2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (listener!=null){
                        listener.animaEnd();
                    }
                }
            });
        }
    }
    private void startAutoInAnimator(View view, int index){
        if (index==0) {
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0F);
            //组合动画方式1
            AnimatorSet set = new AnimatorSet();
            set.play(animator2);
            set.setDuration(1000);
            set.start();
        }else if (index==1) {
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0F);
            //组合动画方式1
            AnimatorSet set = new AnimatorSet();
            set.play(animator2);
            set.setDuration(1000);
            set.start();
        }else if (index==2) {
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0f,0f, 1.0F);
            //组合动画方式1
            AnimatorSet set = new AnimatorSet();
            set.play(animator2);
            set.setDuration(2000);
            set.start();
        }
    }
    private void startInAnimator(View view, int index){
        if (index==0) {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationX", -100.0f, 0f);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0F);
            //组合动画方式1
            AnimatorSet set = new AnimatorSet();
            set.play(animator1).with(animator2);
            set.setDuration(100);
            set.start();
        }else if (index==1) {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationX", -100.0f, 0f);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0F);
            //组合动画方式1
            AnimatorSet set = new AnimatorSet();
            set.play(animator1).with(animator2);
            set.setDuration(100);
            set.start();
        }else if (index==2) {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationX", -100.0f, 0f);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0F);
            //组合动画方式1
            AnimatorSet set = new AnimatorSet();
            set.play(animator1).with(animator2);
            set.setDuration(500);
            set.start();
        }
    }
    //3. 精确计算文字的宽度：
    private int getTextWidth( String str)
    {
        double iSum = 0;
        if(str != null && !str.equals(""))
        {
            int len = str.length();
            float  widths[] = new float[len];
            mPaint.getTextWidths(str, widths);
            for(int i = 0; i < len; i++)
            {
                iSum += Math.ceil(widths[i]);
            }
        }
        return (int) iSum;
    }
    /*
    把华氏温度转换成摄氏温度
     */
    private String changeToC(String tempF){
        int tempFi = 0;
        try {
            if (tempF.contains("°")){
                tempFi = Integer.parseInt(tempF.replace("°",""));
            }else {
                tempFi = Integer.parseInt(tempF.replace("°",""));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        int tempC = (int) ((tempFi-32)/1.8);
        return tempC+"°";
    }
}
