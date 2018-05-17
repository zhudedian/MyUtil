package com.zdd.myutil.view.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zdd.myutil.view.weather.icon.Cloudy;
import com.zdd.myutil.view.weather.icon.Lightning;
import com.zdd.myutil.view.weather.icon.PartCloudy;
import com.zdd.myutil.view.weather.icon.Rain;
import com.zdd.myutil.view.weather.icon.Sun;


/**
 * Created by yd on 2018/4/26.
 */

public class FirstPageWeather extends ViewGroup {



    private Paint datePaint,highTPaint,lowTPaint;
    private float viewSpan,viewWidth;
    private int viewCount = 4;
    private int width,height;
    private int paddingLeft,paddingRight,paddingTop,paddingBottom;

    String[] viewType = new String[]{"Cloudy","Rainy","Partly cloudy","Lightning"};
    String[] dates = new String[]{"Fri","Sat","Sun","Mon"};
    String[] lowTemps = new String[]{"49°","48°","46°","44°"};
    String[] highTemps = new String[]{"64°","53°","54°","56°"};

    public FirstPageWeather(Context context) {
        this(context, null);
    }

    public FirstPageWeather(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FirstPageWeather(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        datePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        datePaint.setColor(Color.WHITE);
        datePaint.setAlpha(255);
        datePaint.setTextAlign(Paint.Align.CENTER);
        datePaint.setTextSize(50);
        datePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        datePaint.setStrokeWidth(1);
        datePaint.setStrokeCap(Paint.Cap.BUTT);
        datePaint.setStrokeJoin(Paint.Join.BEVEL);

        highTPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highTPaint.setColor(Color.WHITE);
        highTPaint.setAlpha(255);
        highTPaint.setTextAlign(Paint.Align.CENTER);
        highTPaint.setTextSize(75);
        highTPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        highTPaint.setStrokeWidth(1.5f);
        highTPaint.setStrokeCap(Paint.Cap.BUTT);
        highTPaint.setStrokeJoin(Paint.Join.BEVEL);

        lowTPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lowTPaint.setColor(Color.WHITE);
        lowTPaint.setAlpha(255);
        lowTPaint.setTextAlign(Paint.Align.CENTER);
        lowTPaint.setTextSize(65);
        lowTPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        lowTPaint.setStrokeWidth(0.2f);
        lowTPaint.setStrokeCap(Paint.Cap.BUTT);
        lowTPaint.setStrokeJoin(Paint.Join.BEVEL);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
        width = w-paddingLeft-paddingRight;
        height = h-paddingTop-paddingBottom;
        viewSpan = width*8/100;
        viewWidth =  (width-viewSpan*(viewCount+1))/viewCount;
//        initData();

    }

    private void initData(){
//        if (templateRuntimeDirective!=null) {
//            for (int i = 0; i < viewCount; i++) {
//                viewType[i] = templateRuntimeDirective.getDirective().getPayload().getWeatherForecast()
//                        .get(i).getImage().getContentDescription();
//                dates[i] = templateRuntimeDirective.getDirective().getPayload().getWeatherForecast()
//                        .get(i).getDay();
//                highTemps[i] = templateRuntimeDirective.getDirective().getPayload().getWeatherForecast()
//                        .get(i).getHighTemperature();
//                lowTemps[i] = templateRuntimeDirective.getDirective().getPayload().getWeatherForecast()
//                        .get(i).getLowTemperature();
//            }
//            initview();
//            postInvalidate();
//        }
    }
    private void initview() {

        removeAllViews();
        for (int i = 0;i<4;i++) {
            if (viewType[i].equals("Cloudy")) {
                final View view = new Cloudy(getContext());
                addView(view);
            }else if (viewType[i].equals("Rainy")){
                final View view = new Rain(getContext());
                addView(view);
            }else if (viewType[i].equals("Partly cloudy")){
                final View view = new PartCloudy(getContext());
                addView(view);
            }else if (viewType[i].equals("Sunny")){
                final View view = new Sun(getContext());
                addView(view);
            }else if (viewType[i].equals("Lightning")){
                final View view = new Lightning(getContext());
                addView(view);
            }
        }
    }

//    private TemplateRuntimeDirective templateRuntimeDirective;
//
//    public void setTemplateRuntimeDirective(TemplateRuntimeDirective templateRuntimeDirective){
//        this.templateRuntimeDirective = templateRuntimeDirective;
//        initData();
//    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();


        float spanT = viewSpan/2+viewWidth/2;
        for (int i = 0;i<viewCount;i++) {
            canvas.drawText(dates[i],viewSpan/2+spanT+i*spanT*2,height*23/100,datePaint);
            canvas.drawText(changeToC(highTemps[i]),viewSpan/2+spanT+i*spanT*2,height*78/100,highTPaint);
            canvas.drawText(changeToC(lowTemps[i]),viewSpan/2+spanT+i*spanT*2,height*90/100,lowTPaint);
        }


        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.layout((int)((viewWidth+viewSpan)*i+viewSpan), height * 33 / 100, (int)(viewWidth+viewSpan)*(i+1), height * 65 / 100);

        }
    }

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