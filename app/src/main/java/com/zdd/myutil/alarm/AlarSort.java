package com.zdd.myutil.alarm;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by yd on 2018/4/3.
 */

public class AlarSort implements Comparator<AlarEvent> {
    private Collator collator = Collator.getInstance(Locale.CHINA);

    public static void sort(List<AlarEvent> list){
        Collections.sort(list,new AlarSort());
    }

    @Override
    public int compare(AlarEvent alarEvent1 , AlarEvent alarEvent2){
        int value = collator.compare(String.valueOf(alarEvent1.getEvnetTime()),String.valueOf(alarEvent2.getEvnetTime()));
        if (value<0){
            return -1;
        }else if (value>0){
            return 1;
        }else {
            return 1;
        }
    }
}
