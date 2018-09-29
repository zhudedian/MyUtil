package com.zdd.myutil.socket;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.zdd.andserver3.http.HttpRequest;
import com.zdd.andserver3.http.HttpResponse;
import com.zdd.andserver3.inter.RequestHandler;
import com.zdd.myutil.R;
import com.zdd.myutil.app.App;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;

public class MyRequestHandle implements RequestHandler{

    public void handle(HttpRequest request,HttpResponse response){

        String comments = unicodetoString(request.getFirstHeader("comments").getValue());
        Log.i("edong","comments="+comments);
        Resources res = App.getContext().getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
        response.setStatus(200,
                "Content-Type","image/png",
                "Content-Disposition","inline; filename=\"ic_launcher.png\"",
                "Content-Length" , ""+bmp.getByteCount());
        response.sendString("请求成功");

        response.sendString("你好");
        response.sendString("真号");
        response.sendString("结束");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        response.write(baos.toByteArray());
    }

    public String unicodetoString(String s){
        String ss[] =  s.split("\\\\u");
        StringBuilder sb=new StringBuilder(ss[0]);
        for (int i = 1; i<ss.length; i++) {

            String uCode = ss[i].substring(0,4);
            StringBuffer sCode = new StringBuffer(ss[i]);
            sCode.delete(0,4);
            sb.append((char)Integer.parseInt(uCode, 16));
            sb.append(sCode);

        }

        return sb.toString();
    }
}
