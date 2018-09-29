package com.zdd.myutil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zdd.andserver3.AndServer;
import com.zdd.annotation.FindId;
import com.zdd.annotation.OnClick;
import com.zdd.api.AnnotationUtil;
import com.zdd.myutil.fonts.CDFontsUtil;
import com.zdd.myutil.fonts.CDfonts;
import com.zdd.myutil.frag.FragUtil;
import com.zdd.myutil.java.StringUtil;
import com.zdd.myutil.media.MediaUtil;
import com.zdd.myutil.media.VoiceHelper;
import com.zdd.myutil.socket.MyRequestHandle;
import com.zdd.myutil.socket.Online;
import com.zdd.myutil.view.TestView;
import com.zdd.myutil.view.custom.viewpager.OrientedViewPager;
import com.zdd.myutil.view.custom.viewpager.VerticalStackTransformer;
import com.zdd.myutil.view.main.MainWeatherPage;

import java.io.File;
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

        textView.setText(getIP());

        new AndServer.Build()
                .port(8088)
                .registerHandler("home",new MyRequestHandle())
                .build()
                .createServer()
                .start();
    }
    @OnClick({R.id.textview,R.id.textview2})
    void onClick(View view){
        switch (view.getId()){
            case R.id.textview:

                Toast.makeText(MainActivity.this,"test click 1",Toast.LENGTH_LONG).show();
                break;
            case R.id.textview2:

//                MediaUtil.speech("hello boy");
                Toast.makeText(MainActivity.this,"test click 2",Toast.LENGTH_LONG).show();
                break;
        }
    }

    private String getIP(){
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        return intToIp(info.getIpAddress());
    }
    private String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }
}
