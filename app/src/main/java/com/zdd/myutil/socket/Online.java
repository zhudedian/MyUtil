package com.zdd.myutil.socket;

import android.os.Handler;
import android.os.Message;



public class Online {
    private static SocketServer server;

    public static boolean isOnline = false;

    private static int hasNew = 0;


    public static void startListener(){
        try {
            if(server==null) {
                server = new SocketServer(new MyHandle(),8088);
            }
            /**socket服务端开始监听*/
            server.beginListen ( );

        }catch (Exception e){
//            Toast.makeText ( MainActivity.this,"请输入数字", Toast.LENGTH_SHORT ).show ();

            e.printStackTrace ();
        }
    }

    public static void send(String msg){
        if (server!=null){
            server.sendMessage(msg);
        }
    }


    private static class MyHandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
//                Log.i("taggg",msg.obj.toString());
            String info = msg.obj.toString();
            if (info.contains("c")){

            }
        }

    }

    public static void notifyHasNew(){
        hasNew++;
    }



}
