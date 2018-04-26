package com.zdd.myutil.view.wzq;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.zdd.myutil.MainActivity;

import java.util.List;

/**
 * Created by yd on 2018/4/12.
 */

public class CheckerboardView extends ViewGroup {
    private String TAG = "CheckerboardView";
    private Paint mPaint;
    private MainActivity mainActivity;
//    private Gobang gobang;
    private CheckerPieceView lastPiece;
    private int strokeWidth = 5;
    private float scale = 0.8f;
    private int gridNumX,gridNumY;
    private float intervalX,intervalY;
    private int width,height;
    private float padding;
    private boolean isStart,isEnded;
    private CheckerPieceView[][] pieceViews = new CheckerPieceView[15][15];
    private int paddingLeft,paddingRight,paddingTop,paddingBottom;


    public CheckerboardView(Context context) {
        this(context, null);
    }

    public CheckerboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckerboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Checkerboard);
//        gridNumX = typedArray.getInteger(R.styleable.Checkerboard_grid_numX,1);
//        gridNumY = typedArray.getInteger(R.styleable.Checkerboard_grid_numY,1);
//        padding = typedArray.getDimension(R.styleable.Checkerboard_padding,0);
//        typedArray.recycle();
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAlpha(255);

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStrokeCap(Paint.Cap.BUTT);//没有
        mPaint.setStrokeJoin(Paint.Join.BEVEL);//直线
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mainActivity = (MainActivity) getContext();
        init();

        Log.i(TAG,"gridNumX="+gridNumX+"gridNumY="+gridNumY);
    }

    private void init() {



//        paddingLeft = DpUtil.dip2px(getContext(),padding);
//        paddingRight = DpUtil.dip2px(getContext(),padding);
//        paddingTop = DpUtil.dip2px(getContext(),padding);
//        paddingBottom = DpUtil.dip2px(getContext(),padding);
//        gobang = new Gobang(new Gobang.ComputerPieceListener() {
//            @Override
//            public void result(int x, int y) {
//                pieceViews[x][y].setColor(Color.BLACK);
//                pieceViews[x][y].addStar();
//                if (lastPiece!=null){
//                    lastPiece.removeStar();
//                    lastPiece = pieceViews[x][y];
//                }else {
//                    lastPiece = pieceViews[x][y];
//                }
//                isStart = true;
//            }
//
//            @Override
//            public void end(String info) {
//                isEnded = true;
//                mainActivity.setText(info);
//                ((MainActivity)getContext()).restart.setVisibility(VISIBLE);
//            }
//        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w-paddingLeft-paddingRight;
        height = h-paddingTop-paddingBottom;
        if (gridNumX<1){
            gridNumX = 1;
        }
        if (gridNumY<1){
            gridNumY = 1;
        }
        intervalX = (float) width/gridNumX;
        intervalY = (float) height/gridNumY;
        Log.i(TAG,"width="+width+"height="+height);
        Log.i(TAG,"intervalX="+intervalX+"intervalY="+intervalY);
        Log.i(TAG,"onSizeChanged()");
        initview();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG,"onDraw()");

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        final int count = getChildCount();
//        for (int i = 0; i < count; i++) {
//            //这个很重要，没有就不显示
//            Log.i(TAG,"widthMeasureSpec="+widthMeasureSpec+"heightMeasureSpec="+widthMeasureSpec);
//            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
//        }
        Log.i(TAG,"onMeasure()");
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG,"onFinishInflate()");
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(paddingLeft, paddingTop);
        mPaint.setStyle(Paint.Style.STROKE);//设置画笔的样式
        mPaint.setStrokeJoin(Paint.Join.ROUND);//圆弧
        Path path1 = new Path();
        path1.moveTo(0, 0);
        path1.lineTo(width, 0);
        path1.lineTo(width, height);
        path1.lineTo(0, height);
        path1.close();
        canvas.drawPath(path1, mPaint);

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeJoin(Paint.Join.BEVEL);//直线
        // x轴
        for (int i=1;i<gridNumX;i++){
            float x = intervalX*i;
            canvas.drawLine(x, 0, x, height, mPaint);
        }

        // y轴
        for (int i=1;i<gridNumY;i++){
            float y = intervalY*i;
            canvas.drawLine(0, y, width, y, mPaint);
        }
        canvas.restore();
        super.dispatchDraw(canvas);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        // 记录总高度
        int mTotalHeight = 0;
        int mTotalWidth = 0;
        // 遍历所有子视图
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            mTotalHeight = (int) (0+(i/(gridNumY+1))*intervalY);
            mTotalWidth = (int) (0+(i%(gridNumX+1))*intervalX);
            View childView = getChildAt(i);
            childView.layout(paddingLeft+mTotalWidth-(int) intervalX/2, paddingTop+mTotalHeight-(int) intervalY/2, paddingLeft+mTotalWidth+(int) intervalX/2, paddingTop+mTotalHeight+(int) intervalY/2);



        }
    }
    private void initview() {

        for (int j = 1;j<=15;j++) {
            for (int i = 1;i<=15;i++) {
                final CheckerPieceView view = new CheckerPieceView(getContext(),i,j);
                pieceViews[i-1][j-1]=view;
                addView(view);
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                params.width = (int) (intervalX*scale);
                params.height = (int) (intervalY*scale);
                view.setLayoutParams(params);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG,"x="+view.getCoX()+"y="+view.getCoY());
//                        if (isStart&&!isEnded) {
//                            if (gobang.setAPiece(view.getCoX(), view.getCoY())) {
//                                isStart = false;
//                                view.setColor(Color.WHITE);
//                                view.addStar();
//                                if (lastPiece!=null){
//                                    lastPiece.removeStar();
//                                    lastPiece = view;
//                                }else {
//                                    lastPiece = view;
//                                }
//                                gobang.play();
//                            }
//                        }
                    }
                });
            }

        }
        isStart = true;
        isEnded = false;
        Log.i(TAG,"initview");
    }

    public void reStart(){
        lastPiece.removeStar();
        for (int j = 1;j<=15;j++) {
            for (int i = 1; i <= 15; i++) {
                pieceViews[i-1][j-1].setColor(0);
                pieceViews[i - 1][j - 1].postInvalidate();
            }
        }
//        gobang.restart();
        isStart = true;
        isEnded = false;
    }
    public void forward(){
        if (!isStart){
            return;
        }
//        List<History.Step> steps = gobang.forward();
        lastPiece.removeStar();
        for (int j = 1;j<=15;j++) {
            for (int i = 1; i <= 15; i++) {
                pieceViews[i-1][j-1].setColor(0);
                pieceViews[i - 1][j - 1].postInvalidate();
            }
        }
//        for (int i = 0;i<steps.size();i++){
//            History.Step step = steps.get(i);
//            CheckerPieceView view = pieceViews[step.x][step.y];
//            if (step.piece=="○"){
//                view.setColor(Color.BLACK);
//            }else {
//                view.setColor(Color.WHITE);
//            }
//            if (i==steps.size()-1){
//                view.addStar();
//                lastPiece = view;
//            }
//        }
    }
    public void next(){
        if (!isStart){
            return;
        }
//        List<History.Step> steps = gobang.next();
        lastPiece.removeStar();
        for (int j = 1;j<=15;j++) {
            for (int i = 1; i <= 15; i++) {
                pieceViews[i-1][j-1].setColor(0);
                pieceViews[i - 1][j - 1].postInvalidate();
            }
        }
//        for (int i = 0;i<steps.size();i++){
//            History.Step step = steps.get(i);
//            CheckerPieceView view = pieceViews[step.x][step.y];
//            if (step.piece=="○"){
//                view.setColor(Color.BLACK);
//            }else {
//                view.setColor(Color.WHITE);
//            }
//            if (i==steps.size()-1){
//                view.addStar();
//                lastPiece = view;
//            }
//        }
    }
}