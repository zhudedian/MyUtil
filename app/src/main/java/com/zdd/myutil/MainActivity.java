package com.zdd.myutil;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zdd.myutil.frag.FragUtil;
import com.zdd.myutil.view.TestView;
import com.zdd.myutil.view.custom.viewpager.OrientedViewPager;
import com.zdd.myutil.view.custom.viewpager.VerticalStackTransformer;
import com.zdd.myutil.view.main.MainWeatherPage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FragUtil.initFragUtil(this);
//        FragUtil.open(FragUtil.Frag.ORIENTEDVIEWPAGER);
        Typeface mTypeface = Typeface.createFromAsset(getAssets(),"font/fangzhengyingbixingshu.TTF");

        TextView textView = (TextView)findViewById(R.id.textview);
        textView.setTypeface(mTypeface);
        textView.setText("生命中有很多东西，能忘掉的叫过去，忘不掉的叫记忆。一个人的寂寞，有时候，很难隐藏得太久，时间太久了，人就会变得沉默，那时候，有些往日的情怀，就找不回来了。 或许，当一段不知疲倦的旅途结束，只有站在终点的人，才会感觉到累。其实我一直都明白，能一直和一人做伴，实属不易。");

        TextView textView2 = (TextView)findViewById(R.id.textview2);
        textView2.setTypeface(mTypeface);
        textView2.setText("生命中有很多东西，能忘掉的叫过去，忘不掉的叫记忆。一个人的寂寞，有时候，很难隐藏得太久，时间太久了，人就会变得沉默，那时候，有些往日的情怀，就找不回来了。 或许，当一段不知疲倦的旅途结束，只有站在终点的人，才会感觉到累。其实我一直都明白，能一直和一人做伴，实属不易。");
        textView2.getPaint().setFakeBoldText(true);
    }
}
