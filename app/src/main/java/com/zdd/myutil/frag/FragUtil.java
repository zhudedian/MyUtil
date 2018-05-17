package com.zdd.myutil.frag;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.zdd.myutil.R;
import com.zdd.myutil.view.custom.MyFrag;


/**
 * Created by yd on 2018/5/3.
 */

public class FragUtil {


    public static FragmentManager fragmentManager;

    private static OrientedViewpagerFrag orientedViewpagerFrag;

    public static MyFrag fromFrag;//当前的Fragment

    public static void initFragUtil(Context context){
        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
    }

    public enum Frag{
        ORIENTEDVIEWPAGER,WEATHER,BTEMP1,BTEMP2,LTEMP1,RPLAYER
    }


    /*
     *根据参数打开Fragment
     */
    public static Fragment open(Frag frag) {
        if (fragmentManager == null){
            throw new NullPointerException(" Maybe you forget called FragUtil.initFragUtil(Context context)");
        }
        if (frag== Frag.ORIENTEDVIEWPAGER){
            showOrientedViewpagerFrag();
        }else {
            throw new RuntimeException("Unable to match "+frag);
        }
        fromFrag.init();
        return fromFrag;
    }


    private static void showOrientedViewpagerFrag(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //初始化fragment并添加到事务中，如果为null就new一个
        if(orientedViewpagerFrag == null){
            orientedViewpagerFrag = new OrientedViewpagerFrag();
            transaction.add(R.id.main_frame_layout, orientedViewpagerFrag);
        }
        //隐藏fragment
        if (fromFrag!=null) {
            transaction.hide(fromFrag);
        }
        //显示需要显示的fragment
        transaction.show(orientedViewpagerFrag);
        //保存当前的fragment
        fromFrag = orientedViewpagerFrag;
        transaction.commit();
    }

}
