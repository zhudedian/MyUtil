package com.zdd.myutil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

@FindId(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @FindId(R.id.textview)
    TextView textView;
    @FindId(R.id.textview2)
    TextView textView2;

    private OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AnnotationUtil.inject(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().header("Content-Type","application/json;Charset=UTF-8")
                        .method("POST",RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"),
                                "{\n" +"\"VER\":\"01\",\n" +
                                    "\"CUEI\":\"181234561234567\",\n" +
                                    "\"MAC\":\"24:e2:71:f4:d7:b0\",\n" +
                                    "\"IP\":\"192.168.0.1\",\n" +
                                    "\"UPLINKMAC\":\"01:02:03:04:05:06\",\n" +
                                    "\"LINK\":\"1\",\n" +
                                    "\"FWVER\":\"11.2.5(15D55)\",\n" +
                                    "\"DATE\":\"2018-07-01 12:05:30\",\n" +
                                    "\"SIGN\":\"B204E0614015092F4F07FE1E772CDDC4\"\n" +
                                    "}"))
                        .url("http://122.96.25.242:18088/terminal/report/terminalInfo.do").build();
                Call call = okHttpClient.newCall(request);
                Response response = null;
                try {
                    response = call.execute();
                    String result = response.body().string();
                    Message message = mHandler.obtainMessage();
                    message.what = 0;
                    Bundle data = new Bundle();
                    data.putString("result",result);
                    message.setData(data);
                    mHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

//        textView.setText(getIP());

//        new AndServer.Build()
//                .port(8088)
//                .registerHandler("home",new MyRequestHandle())
//                .build()
//                .createServer()
//                .start();
    }
    private  String post(String url,String params)throws IOException {
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response="";
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Charset", "utf-8");
            conn.setRequestProperty("Content-Type", "application/json;Charset=UTF-8");
//            conn.setRequestProperty("connection", "keep-alive");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //POST请求
            out = new OutputStreamWriter(
                    conn.getOutputStream());
            out.write(params);
            out.flush();
            //读取响应
            Log.i("edong","conn.getResponseCode()="+conn.getResponseCode());

            if (conn.getResponseCode()==500){
                reader = new BufferedReader(new InputStreamReader(
                        conn.getErrorStream()));
            }else {
                reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
            }
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                response+=lines;
            }
            reader.close();
            // 断开连接
            conn.disconnect();


        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

        return response;
    }
    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    Bundle data = msg.getData();
                    if (data == null) {
                        return;
                    }
                    textView.setText(data.getString("result"));
//                    Toast.makeText(MainActivity.this, "连接成功！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:

//                    Toast.makeText(MainActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();

                    break;
                default:
                    break;
            }

        }
    };
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
    private class ReqBody extends RequestBody {
        String event;
        public ReqBody(String event){
            this.event = event;
        }
        @Nullable
        @Override
        public MediaType contentType() {
            return null;
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            if (event!=null)
                sink.write(event.getBytes());
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
