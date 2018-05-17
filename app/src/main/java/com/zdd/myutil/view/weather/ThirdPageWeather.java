package com.zdd.myutil.view.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zdd.myutil.R;
import com.zdd.myutil.view.weather.icon.Cloudy;
import com.zdd.myutil.view.weather.icon.Lightning;
import com.zdd.myutil.view.weather.icon.PartCloudy;
import com.zdd.myutil.view.weather.icon.Rain;
import com.zdd.myutil.view.weather.icon.Sun;

/**
 * Created by yd on 2018/4/27.
 */

public class ThirdPageWeather extends ViewGroup {
    private Paint mPaint, linePaint, textPaint;
    private int width, height;
    private int paddingLeft, paddingRight, paddingTop, paddingBottom;

    private String highTemp = "78°";
    private String lowTemp = "52°";
    private String viewType = "Lightning";
    private String curTemp = "68°";
    private String subTitle = "Thursday, April 26, 2018";
    private String mainTitle = "Weather In "+"East Queen Anne, WA";
    private String info = "RealFeel:15°\nWind:N 0km/h\nPrecipitation:0%";

    public ThirdPageWeather(Context context) {
        this(context, null);
    }

    public ThirdPageWeather(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThirdPageWeather(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(255);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(180);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeJoin(Paint.Join.BEVEL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setAlpha(255);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(30);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setStrokeWidth(1);
        textPaint.setStrokeCap(Paint.Cap.BUTT);
        textPaint.setStrokeJoin(Paint.Join.BEVEL);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.WHITE);
        linePaint.setAlpha(255);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(1);
        linePaint.setStrokeCap(Paint.Cap.BUTT);
        linePaint.setStrokeJoin(Paint.Join.BEVEL);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
        width = w - paddingLeft - paddingRight;
        height = h - paddingTop - paddingBottom;
        initview();

    }

    private void initData() {
//        if (templateRuntimeDirective != null) {
//            curTemp = templateRuntimeDirective.getDirective().getPayload().getCurrentWeather();
//            viewType = templateRuntimeDirective.getDirective().getPayload().getCurrentWeatherIcon().getContentDescription();
//            subTitle = templateRuntimeDirective.getDirective().getPayload().getTitle().getSubTitle();
//            mainTitle = "Weather In "+templateRuntimeDirective.getDirective().getPayload().getTitle().getMainTitle();
//            initview();
//        }
    }

    public void fresh(String windSpeed){
        info = "RealFeel:" +curTemp+
                "\nWind:" +windSpeed+
                "km/h\nPrecipitation:0%";
        initview();
        postInvalidate();
    }


    private void initview() {
        removeAllViews();

        if (viewType.equals("Cloudy")) {
            final View view = new Cloudy(getContext());
            addView(view);
        } else if (viewType.equals("Rainy")) {
            final View view = new Rain(getContext());
            addView(view);
        } else if (viewType.equals("Partly cloudy")) {
            final View view = new PartCloudy(getContext());
            addView(view);
        } else if (viewType.equals("Sunny")) {
            final View view = new Sun(getContext());
            addView(view);
        } else if (viewType.equals("Lightning")) {
            final View view = new Lightning(getContext());
            addView(view);
        } else if (viewType.equals("Partly cloudy night")) {
            final View view = new PartCloudy(getContext());
            addView(view);
        }
        TextView subTitleView = new TextView(getContext());
        subTitleView.setTextColor(getResources().getColor(R.color.white));
        subTitleView.setText(subTitle);
        subTitleView.setTextSize(32);
        addView(subTitleView);

        TextView mainTitleView = new TextView(getContext());
        mainTitleView.setTextColor(getResources().getColor(R.color.white));
        mainTitleView.setText(mainTitle);
        mainTitleView.setTextSize(32);
        addView(mainTitleView);

        TextView infoView = new TextView(getContext());
        infoView.setTextColor(getResources().getColor(R.color.white));
        infoView.setText(info);
        infoView.setTextSize(50);
        addView(infoView);

        postInvalidate();
    }

//    private TemplateRuntimeDirective templateRuntimeDirective;
//
//    public void setTemplateRuntimeDirective(TemplateRuntimeDirective templateRuntimeDirective) {
//        this.templateRuntimeDirective = templateRuntimeDirective;
//        initData();
//
//    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();


        Path path = new Path();
        path.moveTo(width * 33 / 100, height * 58 / 100);
        path.lineTo(width * 33 / 100, height * 85 / 100);

        canvas.drawPath(path, linePaint);
        canvas.drawText(changeToC(curTemp), width * 20 / 100, height * 84 / 100-1, mPaint);
//        canvas.drawText(subTitle,width * 18 / 100,height * 10 / 100,textPaint);


        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (i==0) {
                childView.layout(width * 10 / 100, height * 27 / 100, width * 25 / 100, height * 70 / 100);
            }else if (i==1){
                childView.layout(width * 8 / 100-2, height * 8 / 100, width * 45 / 100, height * 20 / 100);
            }else if (i==2){
                childView.layout(width * 8 / 100-2, height * 14 / 100-2, width * 45 / 100, height * 20 / 100);
            }else if (i==3){
                childView.layout(width * 36 / 100, height * 60 / 100, width * 65 / 100, height * 90 / 100);
            }
        }
    }

    private String changeToC(String tempF) {
        int tempFi = 0;
        try {
            if (tempF.contains("°")) {
                tempFi = Integer.parseInt(tempF.replace("°", ""));
            } else {
                tempFi = Integer.parseInt(tempF.replace("°", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int tempC = (int) ((tempFi - 32) / 1.8);
        return tempC + "°";
    }
}
