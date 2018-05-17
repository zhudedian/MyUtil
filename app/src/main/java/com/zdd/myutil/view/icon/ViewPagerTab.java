package com.zdd.myutil.view.icon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.zdd.myutil.R;


/**
 * Created by yd on 2018/5/15.
 */

public class ViewPagerTab extends View {

    private Paint mPaint;
    private float radius1, radius2;
    private float paintWidth = 1f;
    private float width,height;
    private float paddingLeft,paddingRight,paddingTop,paddingBottom;
    private int count = 2;
    private int position = 0;
    private ViewPager viewPager;


    public ViewPagerTab(Context context) {
        this(context, null);
    }

    public ViewPagerTab(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(255);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(paintWidth);
        mPaint.setStrokeCap(Paint.Cap.BUTT);//没有
        mPaint.setStrokeJoin(Paint.Join.BEVEL);//直线

    }
    public void setViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(pageChangeListener);
    }
    private void init() {
        paddingLeft = getPaddingLeft()+paintWidth;
        paddingRight = getPaddingRight()+paintWidth;
        paddingTop = getPaddingTop()+paintWidth;
        paddingBottom = getPaddingBottom()+paintWidth;

        width = getWidth()-paddingLeft-paddingRight;
        height = getHeight()-paddingTop-paddingBottom;
        if (viewPager!=null){
            count = viewPager.getAdapter().getCount();
        }
        if (count == 0){
            radius1 = 0;
        }else if (count ==1){
            radius1 = width/2;
        }else {
            radius1 = width/(2*(count*2-1));
        }
        if (radius1>height/2){
            radius1 = height/2;
        }



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        if (count!=0){
            canvas.translate(paddingLeft+radius1, paddingTop+radius1);
            for (int i=0;i<count;i++){
                Path path1 = new Path();
                path1.addCircle(i*4*radius1,0,radius1, Path.Direction.CCW);
                if (i == position){
                    mPaint.setColor(Color.WHITE);
                }else {
                    mPaint.setColor(getResources().getColor(R.color.viewpager_tab_gray));
                }
                canvas.drawPath(path1,mPaint);
            }
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            ViewPagerTab.this.position = position;
            postInvalidate();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
