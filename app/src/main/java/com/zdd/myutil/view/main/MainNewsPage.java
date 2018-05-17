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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yd on 2018/5/10.
 */

public class MainNewsPage extends ViewGroup {
    private Paint mPaint;
    private int textSize = 80;
    private int paintWidth = 3;
    private int width,height;
    private int leftSpan;
    private int paddingLeft,paddingRight,paddingTop,paddingBottom;

    private String newsTitle = "The Latest in Politics";
    private String newsTime = "6 hours ago";

    private float alpha = 0;
    private boolean firstIn = true;
    private TryText tryText;
    private List<View> childViews = new ArrayList<>();
    private Type type = Type.NOLMAL;

    public enum Type{
        MOVE,NOLMAL,AUTOIN,AUTOOUT
    }


    public MainNewsPage(Context context) {
        this(context, null);
    }

    public MainNewsPage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainNewsPage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    public void setAlpha(float alpha){
        this.alpha = alpha;
        if (childViews!=null&&childViews.size()>0) {
            for (View view : childViews) {
                view.setAlpha(alpha);
            }
        }
    }
    public void fresh(Type type){
        this.type = type;
        initview();
    }
    private void initview() {
        removeAllViews();
        TextView textView = new TextView(getContext());
        textView.setText(newsTitle);
        textView.setTextSize(80);
        textView.setTextColor(Color.WHITE);
        addView(textView);
        textView = new TextView(getContext());
        textView.setText(newsTime);
        textView.setTextSize(30);
        textView.setTextColor(Color.WHITE);
        addView(textView);
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
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();

//        mPaint.setTextSize(80);
//        canvas.drawText(newsTitle,leftSpan,height*50/100,mPaint);
//        mPaint.setTextSize(30);
//        canvas.drawText(newsTime,leftSpan,height*75/100,mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v.getVisibility() != View.GONE) {
                measureChild(v, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        childViews.clear();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (i==0){
                childView.layout(leftSpan , (int)(height*0.4f),
                        leftSpan + childView.getMeasuredWidth(), (int)(height*0.4f)+childView.getMeasuredHeight());
            }else if (i==1){
                childView.layout(leftSpan , (int)(height*0.65f),
                        leftSpan + childView.getMeasuredWidth(), (int)(height*0.65f)+childView.getMeasuredHeight());
            }else if (i==2) {
                childView.layout(leftSpan, height - 50,
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
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0f,0f, 1.0F);
            //组合动画方式1
            AnimatorSet set = new AnimatorSet();
            set.play(animator2);
            set.setDuration(2000);
            set.start();
        }else if (index==2) {
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0f,0f, 0f,1.0F);
            //组合动画方式1
            AnimatorSet set = new AnimatorSet();
            set.play(animator2);
            set.setDuration(3000);
            set.start();
        }
    }
    private void startInAnimator(View view, int index){
        if (index==0) {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationX", 100.0f, 0f);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0F);
            //组合动画方式1
            AnimatorSet set = new AnimatorSet();
            set.play(animator1).with(animator2);
            set.setDuration(100);
            set.start();
        }else if (index==1) {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationX", 200.0f, 0f);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0F);
            //组合动画方式1
            AnimatorSet set = new AnimatorSet();
            set.play(animator1).with(animator2);
            set.setDuration(300);
            set.start();
        }else if (index==2) {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationX", 300.0f, 0f);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0F);
            //组合动画方式1
            AnimatorSet set = new AnimatorSet();
            set.play(animator1).with(animator2);
            set.setDuration(500);
            set.start();
        }
    }

}
