package com.zdd.myutil.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Done on 2018/3/27.
 */

public class AlarUtil {

    private static boolean isScaningAlar = false;
    public static boolean CLOCK_NOTICE_TIME = false;
    /*
    *处理闹钟返回结果
     */
    public static ResultBean getResultBean(JSONObject jsonObject,final Context context) throws JSONException {
        // add for linggan
        ResultBean resultBean = null;
        String tts = "没听清，请再说一次";
        resultBean = new ResultBean(tts, ResultBean.PlayType.NO, null, 0);
        String timeSpan = "";
        String timeStr = "";
        String dateStr = "";
        String timeSlot = "";
        DateTime dateTime = getDateTime(jsonObject);
        String datetime_interval= "";
        String datetime_date = "";
        String datetime_time = "";
        long repeatTime = 0 ;
        String repeatType = "NO";
        int alarType = 0;
        if (dateTime!=null){
            datetime_date = dateTime.datetime_date;
            datetime_interval = dateTime.datetime_interval;
            datetime_time = dateTime.datetime_time;
        }
        //Log.i("edong","getResultBean");
        if (jsonObject.has("semantic")){
            //Log.i("edong","semantic");
            JSONObject semanticObject = (JSONObject) jsonObject.getJSONArray("semantic").get(0);
            if (semanticObject.has("intent")) {

                String intent = semanticObject.getString("intent");
                //Log.i("edong","intent= "+intent);
                //根据intent来判断是什么操作类型，新建、查询、删除等
                if (intent.equals("CREATE")) {
                    if (semanticObject.has("slots")) {
                        JSONArray slots = semanticObject.getJSONArray("slots");
                        //因为slots的长度和里面的数据顺序是不确定的，先加到一个list集合中
                        List<JSONObject> slotList = parseToList(slots);
                        String value = "";
                        String normValue = "";
                        String repeatValue = "";
                        String event = "";
                        //然后根据name名称去提取对应的数据
                        for (JSONObject slot:slotList){
                            if (slot.getString("name").equals("datetime")){
                                value= slot.getString("value");
                                normValue= slot.getString("normValue");
                            }else if (slot.getString("name").equals("content")){
                                event= slot.getString("value");
                            }else if (slot.getString("name").equals("name")){
                                if (slot.getString("value").equals("reminder")) {
                                    alarType = 2;
                                }else if (slot.getString("value").equals("clock")) {
                                    alarType = 1;
                                }
                            }else if (slot.getString("name").equals("repeat")){
                                repeatValue = slot.getString("value");
                            }
                        }

                        JSONObject normValueObject = new JSONObject(normValue);
                        String suggestDatetime = normValueObject.getString("suggestDatetime").replace("T", " ");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //得到闹钟时间，转成date实例，方便使用
                        Date date = null;
                        try {
                            date = format.parse(suggestDatetime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (date==null){
                            //如果date是null，就说明时间没有，无法继续操作
                            return resultBean;
                        }
                        //判断是不是重复闹钟
                        if (!repeatValue.equals("")) {
                            if (repeatValue.contains("W")) {
                                repeatType = "WEEK";
                                datetime_date = "每"+DateUtil.changeToWeekStr(repeatValue.replace("W",""));
                                if (date.getTime()<System.currentTimeMillis()){
                                    repeatTime = 86400000*7;
                                }
                                if (!value.contains("每")){
                                    value = "每"+value;
                                }
                            } else if (repeatValue.equals("EVERYDAY")) {
                                repeatType = "DAY";
                                datetime_date = "每天";
                                if (date.getTime()<System.currentTimeMillis()){
                                    repeatTime = 86400000;
                                }
                                if (!value.contains("每天")){
                                    value = "每天"+value;
                                }
                            } else if (repeatValue.contains("M")) {
                                int repeatDay = Integer.parseInt(repeatValue.replace("M",""));
                                long nextTime = DateUtil.getNextLatelyTime(0,repeatDay,suggestDatetime);
                                if (nextTime!=0) {
                                    date = new Date(nextTime);
                                }
                                repeatType = "MONTH";
                                datetime_date = "每月"+NumUtil.getNumberStr(Integer.parseInt(repeatValue.replace("M","")))+"日";
                                if (date.getTime()<System.currentTimeMillis()){
                                    nextTime = DateUtil.getNextLatelyTime(1,repeatDay,suggestDatetime);
                                    if (nextTime!=0) {
                                        date = new Date(nextTime);
                                    }
                                }

                            }
                        }
                        //获取json里的回应
                        tts = jsonObject.getJSONObject("answer").getString("text")+event;
                        //timeStr是给每个闹钟加一个标识，便于以后的修改删除
                        timeStr = System.currentTimeMillis() + "";
                        datetime_interval  = DateUtil.getIntervalForTime(date.getTime());
                        if (repeatType.equals("NO")) {
                            datetime_date = DateUtil.getDateStrForTime(date.getTime());
                        }
                        if (dateTime!=null){
                            datetime_time = dateTime.datetime_time;
                            if (datetime_date.equals("")){
                                datetime_date = dateTime.datetime_date;
                            }
                        }
                                //Log.i("edong","interval="+interval);
                        AlarEvent alarEvent = new AlarEvent(alarType,date.getTime()+repeatTime,
                                timeStr, event, value, repeatType,datetime_interval,datetime_date,datetime_time);
                        alarEvent.setRepeatDay(repeatValue);
                        tts = "好的，"+datetime_date+datetime_interval+datetime_time+"我会提醒您"+event;
                        //判断闹钟是否设置过，包括时间、做什么、是否重复，一样就判断为相同闹钟
                        if (isContainsAlar(alarEvent,context)){
                            if (alarType==1) {
                                resultBean = new ResultBean("这个闹钟您已经设置过了", ResultBean.PlayType.NO, null, 0);
                            }else {
                                resultBean = new ResultBean("这个提醒您已经设置过了", ResultBean.PlayType.NO, null, 0);
                            }
                            return resultBean;
                        }else if (isEqualTimeAlar(alarEvent,context)){
                            //如果只是时间相同就把它覆盖，提示用户修改了之前的闹钟
                            AlarEvent oldAlar = getEqualTimeAlar(alarEvent,context);
                            if (oldAlar!=null){
                                resultBean = new ResultBean("已经帮您把"+alarEvent.getDatetime_date()+alarEvent.getInterval()+alarEvent.getDatetime_time()+oldAlar.getEvent()+"的闹钟改成"+alarEvent.getDatetime_date()+alarEvent.getInterval()+alarEvent.getDatetime_time()+alarEvent.getEvent(),
                                        ResultBean.PlayType.NO, null, 0);
                            }else {
                                resultBean = new ResultBean(tts, ResultBean.PlayType.NO, null, 0);
                            }
                            rePlaceAlar(alarEvent,context);
                            new Thread(){
                                public void run(){
                                    AlarUtil.initAlar(context);
                                }
                            }.start();
                            return resultBean;
                        }




                        alarEvent.save();

                        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
                        if (!preferences.getBoolean("data_save_alarevent", false)) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("data_save_alarevent", true);
                            editor.apply();
                        }
                        initAlar(context);

//                for (int i=0;i<12;i++) {
//                    Log.i("edong", i+1+"月天数= " + getDaysByYearMonth(2020,i+1));
//                }
                    }
                }else if (intent.equals("VIEW")) {
                    //查询闹钟
                    if (semanticObject.has("slots")) {
                        JSONArray slots = semanticObject.getJSONArray("slots");
                        //因为slots的长度和里面的数据顺序是不确定的，先加到一个list集合中
                        List<JSONObject> slotList = parseToList(slots);
                        String value = "";
                        String normValue = "";
                        //然后根据name名称去提取对应的数据
                        for (JSONObject slot : slotList) {
                            if (slot.getString("name").equals("datetime")) {
                                value = slot.getString("value");
                                normValue = slot.getString("normValue");
                            }
                        }
                        //Log.i("edong","interval="+interval);
                        if (!normValue.equals("")) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            JSONObject normValueObject = new JSONObject(normValue);
                            String suggestDatetime = normValueObject.getString("suggestDatetime").replace("T", " ");
                            if (dateTime!=null&&dateTime.datetime_time.equals("")) {
                                datetime_interval = dateTime.datetime_interval;
                            }
                            if (!datetime_interval.equals("")){
                                List<AlarEvent> eventList = findintervalAlar(datetime_interval,suggestDatetime,context);
                                if (eventList != null && eventList.size() > 0) {
                                    if (eventList.size() == 1) {
                                        tts = value + "有" + eventList.size() + "个" + eventList.get(0).getDatetime_date()+eventList.get(0).getInterval()+eventList.get(0).getDatetime_time() + eventList.get(0).getEvent() + "的闹钟,";
                                        resultBean = new ResultBean(tts, ResultBean.PlayType.NO, null, 0);
                                        return resultBean;
                                    } else {
                                        tts = value + "有" + eventList.size() + "个闹钟,";
                                        for (int i = 0; i < eventList.size(); i++) {
                                            AlarEvent event1 = eventList.get(i);
                                            tts = tts + NumUtil.getNumberStr(i+1) + "," + event1.getDatetime_date()+event1.getInterval()+event1.getDatetime_time() + event1.getEvent() + ",";
                                        }
                                        resultBean = new ResultBean(tts, ResultBean.PlayType.NO, null, 0);
                                        return resultBean;
                                    }
                                }
                            }
                            if (suggestDatetime.length() == 10) {
                                suggestDatetime = suggestDatetime + " 00:00:00";
                                Date date = null;
                                try {
                                    date = format.parse(suggestDatetime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (date == null) {
                                    //Log.i("edong", "date == null");
                                    return resultBean;
                                }
                                List<AlarEvent> eventList = findDayAlar(date.getTime(), context);
                                if (eventList != null && eventList.size() > 0) {
                                    if (eventList.size()==1){
                                        tts = value + "有" + eventList.size() + "个"+eventList.get(0).getDatetime_date()+eventList.get(0).getInterval()+eventList.get(0).getDatetime_time() + eventList.get(0).getEvent()+"的闹钟,";
                                        resultBean = new ResultBean(tts, ResultBean.PlayType.NO, null, 0);
                                        return resultBean;
                                    }else {
                                        tts = value + "有" + eventList.size() + "个闹钟,";
                                        for (int i = 0; i < eventList.size(); i++) {
                                            AlarEvent event1 = eventList.get(i);
                                            tts = tts + NumUtil.getNumberStr(i+1) + "," + event1.getDatetime_date()+event1.getInterval()+event1.getDatetime_time() + event1.getEvent() + ",";
                                        }
                                        resultBean = new ResultBean(tts, ResultBean.PlayType.NO, null, 0);
                                        return resultBean;
                                    }
                                }
                            }

                            Date date = null;
                            try {
                                date = format.parse(suggestDatetime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (date != null) {
                                AlarEvent alarEvent = getEqualTimeAlar(date.getTime(), context);
                                if (alarEvent != null) {
                                    resultBean = new ResultBean("已经帮您查询到" + alarEvent.getDatetime_date()+alarEvent.getInterval()+alarEvent.getDatetime_time() + alarEvent.getEvent() + "的闹钟", ResultBean.PlayType.NO, null, 0);
                                    return resultBean;
                                } else {
                                    resultBean = new ResultBean("您没有设置" + value + "的闹钟", ResultBean.PlayType.NO, null, 0);
                                    return resultBean;
                                }
                            }
                        }
                    }
                    tts = findAlarEvent(context);
                }else if (intent.equals("CHANGE")){
//                    if (getAlarEventCount(context)==0){
//                        resultBean = new ResultBean("目前您还没有设置闹钟", ResultBean.PlayType.NO, null, 0);
//                    }else if (jsonObject.has("semantic")) {
//                        if (semanticObject.has("slots")) {
//                            JSONArray slots = semanticObject.getJSONArray("slots");
//                            List<JSONObject> slotList = new ArrayList<>();
//                            String fromValue = "";
//                            String fromNormValue = "";
//                            String toValue = "";
//                            String toNormValue = "";
//                            String repeatValue = "";
//                            String event = "";
//                            if (slots.length() == 4) {
//                                slotList.add((JSONObject) slots.get(0));
//                                slotList.add((JSONObject) slots.get(1));
//                                slotList.add((JSONObject) slots.get(2));
//                                slotList.add((JSONObject) slots.get(3));
//                            } else if (slots.length() == 3) {
//                                slotList.add((JSONObject) slots.get(0));
//                                slotList.add((JSONObject) slots.get(1));
//                                slotList.add((JSONObject) slots.get(2));
//                            } else if (slots.length() == 2) {
//                                slotList.add((JSONObject) slots.get(0));
//                                slotList.add((JSONObject) slots.get(1));
//                            } else if (slots.length() == 1) {
//                                slotList.add((JSONObject) slots.get(0));
//                            }
//                            for (JSONObject slot : slotList) {
//                                if (slot.getString("name").equals("fromTime")) {
//                                    fromValue = slot.getString("value");
//                                    fromNormValue = slot.getString("normValue");
//                                } else if (slot.getString("name").equals("toTime")) {
//                                    toValue = slot.getString("value");
//                                    toNormValue = slot.getString("normValue");
//                                }
//                            }
//
//                        }
//                    }
                    resultBean = new ResultBean("暂时还不支持修改闹钟", ResultBean.PlayType.NO, null, 0);
                    return resultBean;
                }else if (intent.equals("CANCEL")){
                    //删除闹钟
                        //Log.i("edong", "intent.equals(CANCEL)");
                    if (getAlarEventCount(context)==0){
                        resultBean = new ResultBean("目前您还没有设置闹钟", ResultBean.PlayType.NO, null, 0);
                        return resultBean;
                    }else if (jsonObject.has("semantic")){
                        if (semanticObject.has("slots")) {
                            JSONArray slots = semanticObject.getJSONArray("slots");
                            //因为slots的长度和里面的数据顺序是不确定的，先加到一个list集合中
                            List<JSONObject> slotList = parseToList(slots);
                            String value = "";
                            String normValue = "";
                            String propertyValue = "";
                            String repeatValue = "";
                            String event = "";
                            for (JSONObject slot : slotList) {
                                if (slot.getString("name").equals("datetime")) {
                                    value = slot.getString("value");
                                    normValue = slot.getString("normValue");
                                }else if (slot.getString("name").equals("property")){
                                    propertyValue = slot.getString("value");
                                }else if (slot.getString("name").equals("repeat")){
                                    repeatValue = slot.getString("value");
                                }
                            }

                            if (!normValue.equals("")) {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                JSONObject normValueObject = new JSONObject(normValue);
                                String suggestDatetime = normValueObject.getString("suggestDatetime").replace("T", " ");
                                if (dateTime!=null&&dateTime.datetime_time.equals("")) {
                                    datetime_interval = dateTime.datetime_interval;
                                }else {
                                    datetime_interval = "";
                                }
                                if (!datetime_interval.equals("")){
                                    List<AlarEvent> eventList = findintervalAlar(datetime_interval,suggestDatetime,context);
                                    if (eventList != null && eventList.size() > 0) {
                                        if (propertyValue.equals("all")){
                                            removeAlar(eventList,context);
                                            resultBean = new ResultBean("好的，已经帮您删除了"+eventList.get(0).getDatetime_date()+eventList.get(0).getInterval()+"全部闹钟", ResultBean.PlayType.NO, null, 0);
                                            return resultBean;
                                        }
                                        if (eventList.size()==1){
                                            removeAlar(eventList,context);
                                            resultBean = new ResultBean("已经帮您删除了"+eventList.get(0).getDatetime_date()+eventList.get(0).getInterval()+eventList.get(0).getDatetime_time()+eventList.get(0).getEvent()+"的一个闹钟", ResultBean.PlayType.NO, null, 0);
                                            return resultBean;
                                        }else {
                                            tts = value+"有"+eventList.size()+"个闹钟,";
                                            for(int i = 0;i<eventList.size();i++){
                                                AlarEvent event1 = eventList.get(i);
                                                tts = tts+NumUtil.getNumberStr(i+1)+","+event1.getDatetime_date()+event1.getInterval()+event1.getDatetime_time()+event1.getEvent()+",";
                                            }
                                            tts = tts +"您要删除哪个呢？";
                                            resultBean = new ResultBean(tts, ResultBean.PlayType.NO, null, 0);
                                            return resultBean;
                                        }
                                    }
                                }
                                if (suggestDatetime.length()==10){
                                    suggestDatetime = suggestDatetime+" 00:00:00";
                                    Date date = null;
                                    try {
                                        date = format.parse(suggestDatetime);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (date == null) {
                                        return resultBean;
                                    }
                                    List<AlarEvent> eventList = findDayAlar(date.getTime(),context);
                                    if (eventList!=null&&eventList.size()>0){
                                        if (propertyValue.equals("all")){
                                            removeAlar(eventList,context);
                                            resultBean = new ResultBean("好的，已经帮您删除了"+eventList.get(0).getDatetime_date()+"全部闹钟", ResultBean.PlayType.NO, null, 0);
                                            return resultBean;
                                        }
                                        if (eventList.size()==1){
                                            removeAlar(eventList,context);
                                            resultBean = new ResultBean("已经帮您删除了"+eventList.get(0).getDatetime_date()+eventList.get(0).getInterval()+eventList.get(0).getDatetime_time()+eventList.get(0).getEvent()+"的一个闹钟", ResultBean.PlayType.NO, null, 0);
                                            return resultBean;
                                        }else {
                                            if (propertyValue.equals("all")){
                                                removeAlar(eventList,context);
                                                resultBean = new ResultBean("好的，已经帮您删除了"+eventList.get(0).getDatetime_date()+"全部闹钟", ResultBean.PlayType.NO, null, 0);
                                                return resultBean;
                                            }
                                            tts = value+"有"+eventList.size()+"个闹钟,";
                                            for(int i = 0;i<eventList.size();i++){
                                                AlarEvent event1 = eventList.get(i);
                                                tts = tts+NumUtil.getNumberStr(i+1)+","+event1.getDatetime_date()+event1.getInterval()+event1.getDatetime_time()+event1.getEvent()+",";
                                            }
                                            tts = tts +"您要删除哪个呢？";
                                            resultBean = new ResultBean(tts, ResultBean.PlayType.NO, null, 0);
                                            return resultBean;
                                        }
                                    }
                                }

                                Date date = null;
                                if (repeatValue.contains("M")) {
                                    String dateT = suggestDatetime.split(" ")[0];
                                    String[] dates = dateT.split("-");
                                    String repeatDate = repeatValue.replace("M", "");
                                    if (!repeatDate.equals(dates[2])) {
                                        suggestDatetime = dates[0] + "-" + dates[1] + "-" + repeatDate + " " + suggestDatetime.split(" ")[1];
                                        try {
                                            date = format.parse(suggestDatetime);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }else {
                                    try {
                                        date = format.parse(suggestDatetime);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (date == null) {
                                    return resultBean;
                                }
                                AlarEvent alarEvent = getEqualTimeAlar(date.getTime(),context);
                                if (alarEvent!=null){
                                    DataSupport.deleteAll(AlarEvent.class,"eventTime = ?",date.getTime()+"");
                                    resultBean = new ResultBean("已经帮您删除了"+alarEvent.getDatetime_date()+alarEvent.getInterval()+alarEvent.getDatetime_time()+alarEvent.getEvent()+"的闹钟", ResultBean.PlayType.NO, null, 0);
                                    return resultBean;
                                }else {
                                    List<AlarEvent> eventList = findForValueAlar(value,context);
                                    List<AlarEvent> eventList2 = findFordateintervaltimeAlar(datetime_date,datetime_interval,datetime_time,context);
                                    for(AlarEvent alarEvent1:eventList2){
                                        if (!eventList.contains(alarEvent1)){
                                            eventList.add(alarEvent1);
                                        }
                                    }
                                    if (eventList!=null&&eventList.size()>0){
                                        removeAlar(eventList,context);
                                        resultBean = new ResultBean("已经帮您删除了"+eventList.size()+"个"+value+"的闹钟", ResultBean.PlayType.NO, null, 0);
                                        return resultBean;
                                    }
                                    resultBean = new ResultBean("您没有设置"+value+"的闹钟", ResultBean.PlayType.NO, null, 0);
                                    return resultBean;
                                }
                            }else {
                                if (propertyValue.equals("all")){
                                    removeAllAlar(context);
                                    resultBean = new ResultBean("好的，已经帮您删除了全部闹钟", ResultBean.PlayType.NO, null, 0);
                                    return resultBean;
                                }
                                if (!repeatValue.equals("")){
                                    List<AlarEvent> eventList = findForRepeatValueAlar(repeatValue,context);
                                    if (eventList!=null&&eventList.size()>0){
                                        if (eventList.size()==1){
                                            removeAlar(eventList,context);
                                            resultBean = new ResultBean("已经帮您删除了"+eventList.get(0).getDatetime_date()+eventList.get(0).getInterval()+eventList.get(0).getDatetime_time()+eventList.get(0).getEvent()+"的一个闹钟", ResultBean.PlayType.NO, null, 0);
                                            return resultBean;
                                        }else {
                                            if (propertyValue.equals("all")){
                                                removeAlar(eventList,context);
                                                resultBean = new ResultBean("好的，已经帮您删除了"+eventList.get(0).getDatetime_date()+"全部闹钟", ResultBean.PlayType.NO, null, 0);
                                                return resultBean;
                                            }
                                            tts = value+"有"+eventList.size()+"个闹钟,";
                                            for(int i = 0;i<eventList.size();i++){
                                                AlarEvent event1 = eventList.get(i);
                                                tts = tts+NumUtil.getNumberStr(i+1)+","+event1.getDatetime_date()+event1.getInterval()+event1.getDatetime_time()+event1.getEvent()+",";
                                            }
                                            tts = tts +"您要删除哪个呢？";
                                            resultBean = new ResultBean(tts, ResultBean.PlayType.NO, null, 0);
                                            return resultBean;
                                        }
                                    }
                                }
                                if (CLOCK_NOTICE_TIME){
                                    resultBean = new ResultBean("好的", ResultBean.PlayType.NO, null, 0);
                                }else {
                                    AlarEvent alarEvent = getARandomAlar(context);
                                    if (alarEvent != null) {
                                        resultBean = new ResultBean("您要删除哪个闹钟呢，比如说，删除" + alarEvent.getDatetime_date()+alarEvent.getInterval()+alarEvent.getDatetime_time() + alarEvent.getEvent() + "的闹钟", ResultBean.PlayType.NO, null, 0);
                                    } else {
                                        resultBean = new ResultBean("您要删除哪个闹钟呢，您应该说删除什么时候的闹钟", ResultBean.PlayType.NO, null, 0);
                                    }
                                }
                                return resultBean;
                            }

                        }
                    }
                }
            }
        }



        resultBean = new ResultBean(tts, ResultBean.PlayType.NO, null, 0);
        return resultBean;
    }
    /*
    *获取时间DateTime
     */
    public static DateTime getDateTime(JSONObject jsonObject) throws JSONException{
        DateTime dateTime = null;
        String datetime_interval= "";
        String datetime_date = "";
        String datetime_time = "";
        if (jsonObject.has("data")) {
            JSONObject dataObject = (JSONObject) jsonObject.getJSONObject("data");
            if (dataObject.has("result")) {
                JSONArray resultJSONArray =  dataObject.getJSONArray("result");
                if (resultJSONArray.length()>0){
                    JSONObject result = (JSONObject) resultJSONArray.get(0);
                    if (result.has("datetime.INTERVAL")){
                        datetime_interval = result.getString("datetime.INTERVAL");
                    }
                    if (result.has("datetime.date")){
                        datetime_date = result.getString("datetime.date");
                    }
                    if (result.has("datetime.time")){
                        datetime_time = result.getString("datetime.time");
                    }
                    dateTime = new DateTime(datetime_date,datetime_interval,datetime_time);
                }

            }
        }
        return dateTime;
    }
    /*
    *获取时间间隔，上午、下午、晚上等
     */
    public static String getIntervalStr(JSONObject jsonObject,boolean ignore) throws JSONException{
        String interval = "";
        if (jsonObject.has("data")) {
            JSONObject dataObject = (JSONObject) jsonObject.getJSONObject("data");
            if (dataObject.has("result")) {
                JSONArray resultJSONArray =  dataObject.getJSONArray("result");
                if (resultJSONArray.length()>0){
                    JSONObject result = (JSONObject) resultJSONArray.get(0);
                    if (result.has("datetime.INTERVAL")&&(!result.has("datetime.time")||ignore)){
                        interval = result.getString("datetime.INTERVAL");
                    }
                }

            }
        }
        return interval;
    }
    /*
     *初始化闹钟，播报到时间闹钟，删除过时闹钟等
     */
    public static void initAlar(final Context context) {
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)||isScaningAlar) {
            return;
        }
        isScaningAlar = true;
        List<AlarEvent> events = DataSupport.findAll(AlarEvent.class);
        if (events!=null&&events.size()>0){
            AlarSort.sort(events);
            List<AlarEvent> outTimeList = new ArrayList<>();
            for (AlarEvent event:events){
//                LogUtil.i(event.getAlarType()
//                        + ","+DateUtil.formatTime(event.getEvnetTime())+","+event.getValue()
//                        +","+event.getRepeatType()
//                        +","+ event.getDatetime_date()
//                        +","+event.getInterval()
//                        +","+event.getDatetime_time()
//                        +","+event.getEvent());

                if (event.getRepeatType().equals("NO")&&!DateUtil.getDateStrForTime(event.getEvnetTime()).equals("")){
                    String dateTime_date = DateUtil.getDateStrForTime(event.getEvnetTime());
                    if (!dateTime_date.equals(event.getDatetime_date())){
                        event.setDatetime_date(dateTime_date);
                        event.save();
                    }
                }
                if (event.getEvnetTime()-System.currentTimeMillis()<-60*1000){
                    String eventTime = event.getTimeStr();

                    if (event.getRepeatType().equals("DAY")){
                        int day = (int) ((System.currentTimeMillis()-event.getEvnetTime())/86400000);
                        event.setEvnetTime(event.getEvnetTime()+86400000*(day+1));
                        event.save();
                    }else if (event.getRepeatType().equals("WEEK")){
                        int week = (int) ((System.currentTimeMillis()-event.getEvnetTime())/(86400000*7));
                        event.setEvnetTime(event.getEvnetTime()+86400000*7*(week+1));
                        event.save();
                    }else if (event.getRepeatType().equals("MONTH")){
                        int repeatDay = Integer.parseInt(event.getRepeatDay().replace("M",""));
                        long nextTime = DateUtil.getNextLatelyTime(1,repeatDay,event.getEvnetTime());
                        if (nextTime!=0) {
                            event.setEvnetTime(nextTime);
                            event.save();
                        }else {
                                outTimeList.add(event);
                                DataSupport.deleteAll(AlarEvent.class, "timeStr = ?", eventTime);
                        }
                    }else if (event.getRepeatType().equals("MIN")){
                        event.setEvnetTime(event.getEvnetTime()+60000);
                        event.save();
                    }else {
                        outTimeList.add(event);
                        DataSupport.deleteAll(AlarEvent.class, "timeStr = ?", eventTime);
                    }
                }else if (event.getEvnetTime()-System.currentTimeMillis()<1*1000){
                    String eventTime = event.getTimeStr();

                    if (event.getRepeatType().equals("DAY")){
                        int day = (int) ((System.currentTimeMillis()-event.getEvnetTime())/86400000);
                        event.setEvnetTime(event.getEvnetTime()+86400000*(day+1));
                        event.save();
                    }else if (event.getRepeatType().equals("WEEK")){
                        int week = (int) ((System.currentTimeMillis()-event.getEvnetTime())/(86400000*7));
                        event.setEvnetTime(event.getEvnetTime()+86400000*7*(week+1));
                        event.save();
                    }else if (event.getRepeatType().equals("MONTH")){
                        int repeatDay = Integer.parseInt(event.getRepeatDay().replace("M",""));
                        long nextTime = DateUtil.getNextLatelyTime(1,repeatDay,event.getEvnetTime());

                        if (nextTime!=0) {
                            event.setEvnetTime(nextTime);
                            event.save();
                        }else {
                            DataSupport.deleteAll(AlarEvent.class, "timeStr = ?", eventTime);
                        }
                    }else if (event.getRepeatType().equals("MIN")){
                        event.setEvnetTime(event.getEvnetTime()+60000);
                        event.save();
                    }else {
                        DataSupport.deleteAll(AlarEvent.class, "timeStr = ?", eventTime);
                    }
//                    LogUtil.i(event.getDatetime_date()+event.getInterval()+event.getDatetime_time()+"时间到了");
                    if (event.getEvent().equals("")){
                        CLOCK_NOTICE_TIME = true;
//                        OperateUtil.getInstance(ResultBean.PlayType.PAUSE).play(context);
//                        MainActivity.ttsSpeaker(event.getDatetime_date()+event.getInterval()+event.getDatetime_time() + "时间到了");
                    }else {
                        CLOCK_NOTICE_TIME = true;
//                        OperateUtil.getInstance(ResultBean.PlayType.PAUSE).play(context);
//                        MainActivity.ttsSpeaker(event.getDatetime_date()+event.getInterval()+event.getDatetime_time()+event.getEvent() + "时间到了");

                    }
                }else if (event.getEvnetTime()-System.currentTimeMillis()<60*1000){
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            context.sendBroadcast(new Intent("com.edong.alarmandnotice.RING"));
                        }
                    },event.getEvnetTime()-System.currentTimeMillis());
                }
            }
            if (outTimeList.size()>0){
                String tts = "您有"+outTimeList.size()+"个过时闹钟，";
                for (int i=0;i<events.size();i++){
                    AlarEvent event = events.get(i);
                    if (event.getAlarType()==1) {
                        tts = tts + NumUtil.getNumberStr(i+1) +"，"+ event.getDatetime_date()+event.getInterval()+event.getDatetime_time() + ",";
                    }else {
                        tts = tts + NumUtil.getNumberStr(i+1) +"，"+ event.getDatetime_date()+event.getInterval()+event.getDatetime_time() +event.getEvent() + ",";
                    }
                }
//                MainActivity.ttsSpeaker(tts);
            }
        }
        isScaningAlar = false;
    }
    /*
    *随机获取一个闹钟
     */
    public static AlarEvent getARandomAlar(Context context){
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return null;
        }
        List<AlarEvent> events = DataSupport.findAll(AlarEvent.class);
        if (events!=null) {
            Random random = new Random();
            int pos = random.nextInt(events.size());
            return events.get(pos);
        }
        return null;
    }
    /*
    *查找时间段所有的闹钟
     */
    public static List<AlarEvent> findintervalAlar(String interval,String suggestDatetime,Context context){
        if (!interval.equals("")){
            List<AlarEvent> eventList = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = suggestDatetime.split(" ")[0];
            long intervalTime = 0;
            if (interval.equals("凌晨")){
                suggestDatetime = dateTime + " 00:00:00";
                intervalTime = 3600000*6;
            }else if (interval.equals("早晨")||interval.equals("早上")){
                suggestDatetime = dateTime + " 06:00:00";
                intervalTime = 3600000*2;
            }else if (interval.equals("上午")){
                suggestDatetime = dateTime + " 08:00:00";
                intervalTime = 3600000*4;
            }else if (interval.equals("中午")){
                suggestDatetime = dateTime + " 12:00:00";
                intervalTime = 3600000*2;
            }else if (interval.equals("下午")){
                suggestDatetime = dateTime + " 14:00:00";
                intervalTime = 3600000*4;
            }else if (interval.equals("傍晚")){
                suggestDatetime = dateTime + " 17:00:00";
                intervalTime = 3600000*2;
            }else if (interval.equals("晚上")){
                suggestDatetime = dateTime + " 19:00:00";
                intervalTime = 3600000*5;
            }else if (interval.equals("深夜")){
                suggestDatetime = dateTime + " 22:00:00";
                intervalTime = 3600000*2;
            }
            Date date = null;
            try {
                date = format.parse(suggestDatetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                eventList = findintervalAlar(date.getTime(), intervalTime, context);
            }
            //Log.i("edong","eventList.size()="+eventList.size());
            suggestDatetime = dateTime + " 00:00:00";
            date = null;
            try {
                date = format.parse(suggestDatetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<AlarEvent> dayList = null;
            if (date!=null){
                dayList = findDayAlar(date.getTime(),context);
            }
            if (dayList!=null&&dayList.size()>0){
                for(AlarEvent event:dayList){
                    //Log.i("edong","event.getInterval()="+event.getInterval());
                    if (interval.equals(event.getInterval())){
                        if (!eventList.contains(event)) {
                            eventList.add(event);
                        }
                    }
                }
            }
            AlarSort.sort(eventList);
            //Log.i("edong","eventList.size()="+eventList.size());
            return eventList;

        }
        return null;
    }
    /*
        *查找date,interval,time相同所有的闹钟
         */
    public static List<AlarEvent> findFordateintervaltimeAlar(String datetime_date,String datetime_interval,String datetime_time,Context context){
        List<AlarEvent> eventList = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return eventList;
        }
        List<AlarEvent> events = DataSupport.findAll(AlarEvent.class);
        if (events!=null) {
            for (AlarEvent event : events) {
                if (event.getDatetime_date().equals(datetime_date)&&event.getInterval().equals(datetime_interval)
                        &&event.getDatetime_time().equals(datetime_time)){
                    eventList.add(event);
                }
            }
            return eventList;
        }
        return eventList;
    }
    /*
        *查找value相同所有的闹钟
         */
    public static List<AlarEvent> findForRepeatValueAlar(String repeatValue,Context context){
        List<AlarEvent> eventList = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return eventList;
        }
        List<AlarEvent> events = DataSupport.findAll(AlarEvent.class);
        if (events!=null) {
            for (AlarEvent event : events) {
                if (event.getRepeatDay().equals(repeatValue)){
                    eventList.add(event);
                }
            }
        }

        return eventList;
    }
    /*
        *查找value相同所有的闹钟
         */
    public static List<AlarEvent> findForValueAlar(String value,Context context){
        List<AlarEvent> eventList = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return eventList;
        }
        List<AlarEvent> events = DataSupport.findAll(AlarEvent.class);
        if (events!=null) {
            for (AlarEvent event : events) {
                if (event.getValue().equals(value)){
                    eventList.add(event);
                }
            }
        }

        return eventList;
    }
    /*
    *查找时间段所有的闹钟
     */
    public static List<AlarEvent> findintervalAlar(long startTime,long intervalTime,Context context){
        List<AlarEvent> eventList = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return eventList;
        }
        List<AlarEvent> events = DataSupport.findAll(AlarEvent.class);
        if (events!=null) {
            long endTime = startTime+intervalTime;
            AlarSort.sort(events);
            for (AlarEvent event : events) {
                if (event.getEvnetTime()>=startTime&&event.getEvnetTime()<endTime){
                    eventList.add(event);
                }
            }
            return eventList;
        }
        return eventList;
    }
    /*
    *查找哪一天所有的闹钟
     */
    public static List<AlarEvent> findDayAlar(long eventTime,Context context){
        List<AlarEvent> eventList = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return eventList;
        }
        List<AlarEvent> events = DataSupport.findAll(AlarEvent.class);
        if (events!=null) {
            long startTime = eventTime;
            long endTime = eventTime+86400000;
            AlarSort.sort(events);
            for (AlarEvent event : events) {
                if (event.getEvnetTime()>=startTime&&event.getEvnetTime()<endTime){
                    eventList.add(event);
                }
            }
            return eventList;
        }
        return eventList;
    }
    /*
    *获取相同时间的闹钟
     */
    public static AlarEvent getEqualTimeAlar(long eventTime,Context context){
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return null;
        }
        List<AlarEvent> events = DataSupport.findAll(AlarEvent.class);
        if (events!=null) {
            AlarSort.sort(events);
            for (AlarEvent event : events) {
                if (event.equallyTime(eventTime)) {
                    return event;
                }
            }
        }
        return null;
    }
    /*
    *获取相同时间的闹钟
     */
    public static AlarEvent getEqualTimeAlar(AlarEvent alarEvent,Context context){
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return null;
        }
        List<AlarEvent> events = DataSupport.findAll(AlarEvent.class);
        if (events!=null) {
            AlarSort.sort(events);
            for (AlarEvent event : events) {
                if (event.equallyTime(alarEvent.getEvnetTime())) {
                    return event;
                }
            }
        }
        return null;
    }
    /*
    *判断是否存在相同时间的闹钟
     */
    public static boolean isEqualTimeAlar(AlarEvent alarEvent,Context context){
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return false;
        }
        List<AlarEvent> events = DataSupport.findAll(AlarEvent.class);
        if (events!=null) {
            AlarSort.sort(events);
            for (AlarEvent event : events) {
                if (event.equallyTime(alarEvent.getEvnetTime())) {
                    return true;
                }
            }
        }
        return false;
    }
    /*
    *判断是否存在相同闹钟，包括时间，做什么，是否重复
     */
    public static boolean isContainsAlar(AlarEvent alarEvent,Context context){
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return false;
        }
        List<AlarEvent> events = DataSupport.findAll(AlarEvent.class);
        if (events!=null) {
            AlarSort.sort(events);
            for (AlarEvent event : events) {
                if (event.equally(alarEvent.getEvnetTime(), alarEvent.getEvent(), alarEvent.getRepeatType())) {
                    return true;
                }
            }
        }
        return false;
    }
    /*
    *获取闹钟个数
     */
    public static int getAlarEventCount(Context context){
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return 0 ;
        }
        List<AlarEvent> events = DataSupport.findAll(AlarEvent.class);
        if (events!=null){
            return events.size();
        }
        return 0;
    }
    /*
    *判断是否存在相同闹钟，包括时间，做什么，是否重复
     */
    public static List<JSONObject> parseToList (JSONArray slots) throws JSONException{
        List<JSONObject> slotList = new ArrayList<>();
        if (slots.length() == 4) {
            slotList.add((JSONObject) slots.get(0));
            slotList.add((JSONObject) slots.get(1));
            slotList.add((JSONObject) slots.get(2));
            slotList.add((JSONObject) slots.get(3));
        } else if (slots.length() == 3) {
            slotList.add((JSONObject) slots.get(0));
            slotList.add((JSONObject) slots.get(1));
            slotList.add((JSONObject) slots.get(2));
        } else if (slots.length() == 2) {
            slotList.add((JSONObject) slots.get(0));
            slotList.add((JSONObject) slots.get(1));
        } else if (slots.length() == 1) {
            slotList.add((JSONObject) slots.get(0));
        }
        return slotList;
    }
    /*
    *返回设置的闹钟信息
     */
    public static String findAlarEvent(Context context){
        String info = "";
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return "还没有设置闹钟，先设置一个吧" ;
        }
        List<AlarEvent> events = DataSupport.findAll(AlarEvent.class);
        AlarSort.sort(events);
        if (events!=null){
            if (events.size()==0){
                return "还没有设置闹钟，先设置一个吧" ;
            }else {
                info = "您好，帮您查到您有以下闹钟，";
                for (int i=0;i<events.size();i++){
                    AlarEvent event = events.get(i);
                    if (event.getAlarType()==1) {
                        info = info + NumUtil.getNumberStr(i+1) +"，"+ event.getDatetime_date()+event.getInterval()+event.getDatetime_time() + ",";
                    }else {
                        info = info + NumUtil.getNumberStr(i+1) +"，"+ event.getDatetime_date()+event.getInterval()+event.getDatetime_time() +event.getEvent() + ",";
                    }
                }

            }
        }
        return info;
    }
    /*
    *修改闹钟，把现有的覆盖
     */
    public static void rePlaceAlar(AlarEvent alarEvent,Context context){
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return ;
        }
        DataSupport.deleteAll(AlarEvent.class,"eventTime = ?",alarEvent.getEvnetTime()+"");
        alarEvent.save();
    }
    /*
    *删除闹钟集合
     */
    public static void removeAlar(List<AlarEvent> list,Context context){
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return ;
        }
        for (AlarEvent event:list){
            DataSupport.deleteAll(AlarEvent.class,"eventTime = ?",event.getEvnetTime()+"");
        }

    }
    /*
    *删除所有的闹钟
     */
    public static void removeAllAlar(Context context){
        SharedPreferences preferences = context.getSharedPreferences("speech_prefers", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("data_save_alarevent",false)) {
            return ;
        }
        DataSupport.deleteAll(AlarEvent.class);
    }

    public static class DateTime{
        public String datetime_interval;
        public String datetime_date;
        public String datetime_time;
        public DateTime(String datetime_date,String datetime_interval,String datetime_time){
            this.datetime_date = datetime_date;
            this.datetime_interval = datetime_interval;
            this.datetime_time = datetime_time;
        }
    }




}
