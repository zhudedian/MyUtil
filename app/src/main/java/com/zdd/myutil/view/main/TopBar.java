package com.zdd.myutil.view.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.zdd.myutil.R;
import com.zdd.myutil.system.util.BrightnessUtil;


/**
 * Created by yd on 2018/5/2.
 */

public class TopBar extends LinearLayout {


    private Context context;
    private LinearLayout allView,topLinear;
    private ImageView homeView,settingsView,disturbView;
    private SeekBar brightSeek;
    private int progress = 0;
    private boolean isShowing = false;



    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.top_bar, this);
        allView = (LinearLayout)findViewById(R.id.top_bar_all);
        topLinear = (LinearLayout)findViewById(R.id.top_bar_linear);
        homeView = (ImageView)findViewById(R.id.pll_home);
        settingsView = (ImageView)findViewById(R.id.pll_settings);
        disturbView = (ImageView)findViewById(R.id.pll_disturb);
        brightSeek = (SeekBar)findViewById(R.id.sb_brightness);
        brightSeek.setMax(255);
        progress = BrightnessUtil.getBrightness(context);
        brightSeek.setProgress(progress);
        setListener();


    }

    private void setListener(){
        allView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        topLinear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        homeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });
        settingsView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });
        disturbView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        brightSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private Context context;
            private int progress = 0;


            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
                handler.removeCallbacks(setBright);
                handler.postDelayed(setBright,100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                BrightnessUtil.setScreenBrightness(context,progress);

            }

            private Handler handler = new Handler();
            Runnable setBright = new Runnable() {
                @Override
                public void run() {
                    BrightnessUtil.setScreenBrightness(context,progress);

                }
            };



        });
    }

    public void init(){
        if (brightSeek!=null){
            int progress = BrightnessUtil.getBrightness(getContext());
            brightSeek.setProgress(progress);
        }
    }

    public boolean isShowing(){
        return isShowing;
    }
    public float getBarHeight(){
        return topLinear.getHeight();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();
//        Log.i("edong",topLinear.getHeight()+"y="+y+"topbar,action="+action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (y<topLinear.getHeight()){
                    startDismissAble();
                }
                else if (y>topLinear.getHeight()){
                    dismiss();
                    return true;
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public void show(){
        if (!isShowing) {
            init();
            setVisibility(VISIBLE);
            isShowing = true;
            ObjectAnimator animator = ObjectAnimator.ofFloat(topLinear, "translationY", -180f, 0f);
            animator.setDuration(150).start();
        }
        startDismissAble();
    }
    public void dismiss(){
        if (isShowing) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(topLinear, "translationY", 0f, -180f);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    TopBar.this.setVisibility(GONE);
                    isShowing = false;
                }
            });
            animator.setDuration(150).start();
        }
    }
    private void startDismissAble(){
        handler.removeCallbacks(dismissAble);
        handler.postDelayed(dismissAble,10000);
    }
    private Handler handler = new Handler();
    private Runnable dismissAble = new Runnable() {
        @Override
        public void run() {
            TopBar.this.dismiss();
        }
    };
}
