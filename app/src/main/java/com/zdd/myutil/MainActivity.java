package com.zdd.myutil;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zdd.annotation.FindId;
import com.zdd.annotation.OnClick;
import com.zdd.api.AnnotationUtil;
import com.zdd.myutil.fonts.CDFontsUtil;
import com.zdd.myutil.fonts.CDfonts;
import com.zdd.myutil.frag.FragUtil;
import com.zdd.myutil.view.TestView;
import com.zdd.myutil.view.custom.viewpager.OrientedViewPager;
import com.zdd.myutil.view.custom.viewpager.VerticalStackTransformer;
import com.zdd.myutil.view.main.MainWeatherPage;

import java.util.ArrayList;
import java.util.List;
@FindId(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @FindId(R.id.textview)
    TextView textView;
    @FindId(R.id.textview2)
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AnnotationUtil.inject(this);
        FragUtil.initFragUtil(this);
        FragUtil.open(FragUtil.Frag.ORIENTEDVIEWPAGER);
        Typeface mTypeface = Typeface.createFromAsset(getAssets(),"font/fangzhengxingshu.ttf");
        textView.setText("1"+CDfonts.SPADES+"2"+CDfonts.CLUBS+"3"+CDfonts.DIAMONDS+"4"+CDfonts.HEART);
        textView2.setTypeface(mTypeface);
        textView2.setText("生命中有很多东西，能忘掉的叫过去，忘不掉的叫记忆。一个人的寂寞，有时候，很难隐藏得太久，时间太久了，人就会变得沉默，那时候，有些往日的情怀，就找不回来了。 或许，当一段不知疲倦的旅途结束，只有站在终点的人，才会感觉到累。其实我一直都明白，能一直和一人做伴，实属不易。");
        textView2.getPaint().setFakeBoldText(true);
        CDFontsUtil.setOcticons(textView);
    }
    @OnClick({R.id.textview,R.id.textview2})
    void clickTest(View view){
        switch (view.getId()){
            case R.id.textview:
                Toast.makeText(MainActivity.this,"test click 1",Toast.LENGTH_LONG).show();
                break;
            case R.id.textview2:
                Toast.makeText(MainActivity.this,"test click 2",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
