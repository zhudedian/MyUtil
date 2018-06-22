package com.zdd.myutil.frag;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zdd.annotation.FindId;
import com.zdd.annotation.OnClick;
import com.zdd.api.AnnotationUtil;
import com.zdd.myutil.MainActivity;
import com.zdd.myutil.R;
import com.zdd.myutil.view.TestView;
import com.zdd.myutil.view.custom.MyFrag;
import com.zdd.myutil.view.custom.viewpager.OrientedViewPager;
import com.zdd.myutil.view.custom.viewpager.VerticalStackTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yd on 2018/5/17.
 */

@FindId(0)
public class OrientedViewpagerFrag extends MyFrag {

//    @FindId(R.id.oriented_viewpager)
//    OrientedViewPager mOrientedViewPager;
    private List<View> views = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_viewpager_oriented, null);
        AnnotationUtil.inject(this,view);

//        mOrientedViewPager = (OrientedViewPager) view.findViewById(R.id.oriented_viewpager);
////设置viewpager的方向为竖直
//        mOrientedViewPager.setOrientation(OrientedViewPager.Orientation.VERTICAL);
//        //设置limit
//        mOrientedViewPager.setOffscreenPageLimit(4);
//        //设置transformer
//        mOrientedViewPager.setPageTransformer(true, new VerticalStackTransformer(getContext()));
//        views.add(new TestView(getContext(), Color.WHITE,"1"));
//        views.add(new TestView(getContext(), Color.RED,"2"));
//        views.add(new TestView(getContext(), Color.GRAY,"3"));
//        views.add(new TestView(getContext(), Color.GREEN,"4"));
//        mOrientedViewPager.setAdapter(new PagerAdapter() {
//            @Override
//            public int getCount() {
//                return views.size();
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object object) {
//                return view == object;
//            }
//            @Override
//            public void destroyItem(ViewGroup view, int position, Object object) {
//                view.removeView(views.get(position));
//            }
//
//            // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
//            @Override
//            public Object instantiateItem(ViewGroup view, int position) {
//                view.addView(views.get(position));
//                return views.get(position);
//            }
//        });
        return view;
    }
    @OnClick({R.id.oriented_viewpager})
    void onClick(View view){
        switch (view.getId()){
            case R.id.oriented_viewpager:
                Toast.makeText(getContext(),"test click 222222",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
