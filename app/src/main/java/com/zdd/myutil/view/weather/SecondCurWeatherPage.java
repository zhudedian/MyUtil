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

import com.edong.myechoshow.ui.displaycard.bean.TemplateRuntimeDirective;

/**
 * Created by yd on 2018/4/27.
 */

public class SecondCurWeatherPage extends ViewGroup {

    private Paint mPaint,linePaint,lowTempPaint;
    private int width,height;
    private int paddingLeft,paddingRight,paddingTop,paddingBottom;

    private String highTemp = "78°";
    private String lowTemp = "52°";
    private String viewType = "Lightning";

    public SecondCurWeatherPage(Context context) {
        this(context, null);
    }

    public SecondCurWeatherPage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecondCurWeatherPage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        lowTempPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lowTempPaint.setColor(Color.WHITE);
        lowTempPaint.setAlpha(255);
        lowTempPaint.setTextAlign(Paint.Align.CENTER);
        lowTempPaint.setTextSize(180);
        lowTempPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        lowTempPaint.setStrokeWidth(1);
        lowTempPaint.setStrokeCap(Paint.Cap.BUTT);
        lowTempPaint.setStrokeJoin(Paint.Join.BEVEL);

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
        width = w-paddingLeft-paddingRight;
        height = h-paddingTop-paddingBottom;
//        initData();

    }

    private void initData(){
        if (templateRuntimeDirective!=null) {
            highTemp = templateRuntimeDirective.getDirective().getPayload().getHighTemperature().getValue();
            lowTemp = templateRuntimeDirective.getDirective().getPayload().getLowTemperature().getValue();
            viewType = templateRuntimeDirective.getDirective().getPayload().getCurrentWeatherIcon().getContentDescription();
            initview();
        }
    }
    private void initview() {
        removeAllViews();

        if (viewType.equals("Cloudy")) {
            final View view = new Cloudy(getContext());
            addView(view);
        }else if (viewType.equals("Rainy")){
            final View view = new Rain(getContext());
            addView(view);
        }else if (viewType.equals("Partly cloudy")){
            final View view = new PartCloudy(getContext());
            addView(view);
        }else if (viewType.equals("Sunny")){
            final View view = new Sun(getContext());
            addView(view);
        }else if (viewType.equals("Lightning")){
            final View view = new Lightning(getContext());
            addView(view);
        }else if (viewType.equals("Partly cloudy night")){
            final View view = new PartCloudy(getContext());
            addView(view);
        }

        postInvalidate();
    }

    private TemplateRuntimeDirective templateRuntimeDirective;

    public void setTemplateRuntimeDirective(TemplateRuntimeDirective templateRuntimeDirective){
        this.templateRuntimeDirective = templateRuntimeDirective;
        initData();

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();


        Path path = new Path();
        path.moveTo(width*33/100,height*58/100);
        path.lineTo(width*33/100,height*80/100);

        canvas.drawPath(path,linePaint);
        canvas.drawText(changeToC(highTemp),width*20/100,height*80/100,mPaint);
        canvas.drawText(changeToC(lowTemp),width*46/100,height*80/100,lowTempPaint);



        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.layout(width*10/100, height * 15 / 100, width*25/100, height * 60 / 100);

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
