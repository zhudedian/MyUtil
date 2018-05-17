package com.zdd.myutil.view.main;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;


import com.zdd.myutil.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yd on 2018/5/10.
 */

public class TryText extends TextView {


    private List<String> tryList;
    private String text;
    public TryText(Context context) {
        this(context, null);
    }

    public TryText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TryText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        fresh();
    }


    public void fresh(){
        if (tryList==null){
            getTryTextList();
        }
        Random random = new Random();
        int position = random.nextInt(tryList.size());
        text = "Try \"Alexa, "+tryList.get(position)+"\"";
        super.setText(text);
    }


    private void getTryTextList(){
        if (tryList==null){
            tryList = new ArrayList<>();
        }
        tryList.clear();
        try {

            //获取信息的方法
            Resources res = getContext().getResources();
            XmlResourceParser xrp = res.getXml(R.xml.alexatry);
            //判断是否已经到了文件的末尾
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String name = xrp.getName();
                    if (name.equals("try")) {
                        //关键词搜索，如果匹配，那么添加进去如果不匹配就不添加，如果没输入关键词“”,那么默认搜索全部
                        String text = xrp.getAttributeValue(0);
                        tryList.add(text);
                    }
                }
                //搜索过第一个信息后，接着搜索下一个
                xrp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
