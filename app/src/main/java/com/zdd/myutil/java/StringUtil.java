package com.zdd.myutil.java;

import android.util.Log;

/**
 * create by zhudedian at 2018/6/21.
 */

public class StringUtil {


    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
                int v = src[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);

        }
        return stringBuilder.toString();
    }
    public static String asciiToString(byte[] bytes) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            builder.append((char)bytes[i]);
        }

        return builder.toString();
    }
    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length - 1) {
                sbu.append((int)chars[i]).append(",");
            }else {
                sbu.append((int)chars[i]);
            }
        }
        return sbu.toString();
    }
    private static String unitoString(String ss){

        Log.i("edong","ss="+ss);
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i*4<=ss.length()-4; i++) {
            String uCode = ss.substring(i*4,(i+1)*4);
            StringBuffer sCode = new StringBuffer(uCode);
            sCode.delete(0,4);
            sb.append((char)Integer.parseInt(uCode, 16));
            sb.append(sCode);
        }
        return sb.toString();
    }
    public static String unicodetoString(String s){
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
    public static String stringToUnicode(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0, length = str.length(); i < length; i++) {
            char c = str.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                stringBuffer.append(String.format("\\u%04x", (int) c));
            } else {
                stringBuffer.append(c);
            }
        }
        String unicode = stringBuffer.toString();
        return unicode;
    }
}
