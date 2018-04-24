package com.zdd.myutil.uart;

import android.serialport.SerialPort;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yd on 2018/4/24.
 */

public class UartUtil {
    private static FileOutputStream mOutputStream;
    private static FileInputStream mInputStream;
    private static SerialPort sp;

    public static void open(){
        if (sp!=null){
            return;
        }
        try {
            sp=new SerialPort(new File("/dev/ttyS2"),115200);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mOutputStream=(FileOutputStream) sp.getOutputStream();
        mInputStream=(FileInputStream) sp.getInputStream();
    }
    public static void send(String sendStr){
        try {
            mOutputStream.write(new String(sendStr).getBytes());
            mOutputStream.write('\r');
            mOutputStream.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void recieve(ReceiveListener listener){
        int size;

        try {
            byte[] buffer = new byte[64];
            if (mInputStream == null) return;
            size = mInputStream.read(buffer);
            if (size > 0) {
                listener.received(buffer, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
    public interface ReceiveListener{
        void received(byte[] buffer,int size);
    }

}
