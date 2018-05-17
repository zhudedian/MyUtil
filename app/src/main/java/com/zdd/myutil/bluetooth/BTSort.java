package com.zdd.myutil.bluetooth;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by yd on 2018/4/21.
 */

public class BTSort implements Comparator<BT> {
    private Collator collator = Collator.getInstance(Locale.CHINA);

    public static void sort(List<BT> list){
        Collections.sort(list,new BTSort());
    }

    @Override
    public int compare(BT bt1 , BT bt2){
        if (bt1.getBtName().equals("Notice info")){
            return 1;
        }else if (bt2.getBtName().equals("Notice info")){
            return -1;
        }else {
            int value = collator.compare(String.valueOf(bt1.isConnected()), String.valueOf(bt2.isConnected()));
            if (value > 0) {
                return -1;
            } else if (value < 0) {
                return 1;
            } else {
                int value2 = collator.compare(String.valueOf(bt1.isPaired()), String.valueOf(bt2.isPaired()));
                if (value2 > 0) {
                    return -1;
                } else if (value2 < 0) {
                    return 1;
                } else {
                    int value3 = collator.compare(bt1.getAddress(), bt2.getAddress());
                    if (value3 > 0) {
                        return 1;
                    } else if (value3 < 0) {
                        return -1;
                    } else {
                        return -1;
                    }
                }
            }
        }
    }
}
