package com.zdd.myutil.view.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yd on 2018/4/23.
 */

public class Cloud extends View {


    private Paint mPaint;
    private int radius1, radius2,radius3,radius4;
    private int paintWidth = 5;
    private int width,height;
    private int paddingLeft,paddingRight,paddingTop,paddingBottom;
    public Cloud(Context context) {
        this(context, null);
    }

    public Cloud(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Cloud(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(255);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(paintWidth);
        mPaint.setStrokeCap(Paint.Cap.BUTT);//没有
        mPaint.setStrokeJoin(Paint.Join.BEVEL);//直线

    }
    private void init() {





        paddingLeft = getPaddingLeft()+paintWidth/2;
        paddingRight = getPaddingRight()+paintWidth/2;
        paddingTop = getPaddingTop()+paintWidth/2;
        paddingBottom = getPaddingBottom()+paintWidth/2;

        width = getWidth()-paddingLeft-paddingRight;
        height = getHeight()-paddingTop-paddingBottom;

        radius1 = width*14/100;
        radius2 = width*17/100;
        radius3 = width*21/100;
        radius4 = width*21/100;
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

        path1.addCircle(radius1,height-radius1,radius1, Path.Direction.CCW);
        path2.addCircle(width*30/100,height-width*21/100,radius2, Path.Direction.CCW);
        path3.addCircle(width*50/100,height-width*33/100,radius3, Path.Direction.CCW);
        path4.addCircle(width-radius4,height-radius4,radius4, Path.Direction.CCW);
        path5.addRect(radius1, height-radius1*2, width -radius4, height, Path.Direction.CCW);

        path1.op(path2, Path.Op.UNION);
        path1.op(path3, Path.Op.UNION);
        path1.op(path4, Path.Op.UNION);
        path1.op(path5, Path.Op.UNION);
        canvas.drawPath(path1,mPaint);

    }
}
