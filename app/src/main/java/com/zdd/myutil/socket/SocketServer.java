package com.zdd.myutil.socket;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zdd.myutil.R;
import com.zdd.myutil.app.App;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Eric on 2017/8/9.
 */

public class SocketServer {
    private static ServerSocket server;
    private Socket socket;
    private InputStream in;
    private String str=null;
    private boolean isClint=false;
    private static int size =400;
    public static boolean beginrecieve = false;
    private  Handler serverHandler;

    /**
     * @steps bind();绑定端口号
     * @effect 初始化服务端
     * @param port 端口号
     * */
    public SocketServer(Handler handler,int port){
        try {
            serverHandler = handler;
            server= new ServerSocket( port );
        }catch (IOException e){
            e.printStackTrace ();
        }

    }

    /**
     * @steps listen();
     * @effect socket监听数据
     * */
    public void beginListen()
    {
        isClint=true;
        new Thread(new Runnable( )
        {
            @Override
            public void run()
            {
                try {
                    /**
                     * accept();
                     * 接受请求
                     * */
                while (true) {
                    Socket soc = server.accept();

                    socket = soc;
                    new ServerThread(soc);
                }
                } catch (Exception e) {
                    e.printStackTrace ( );
//                    socket.isClosed ();
                }
            }
        } ).start ();
    }


    private void outLine(final Socket soc){
        new Thread(new Runnable( ) {
            @Override
            public void run()
            {
                try {
                    PrintWriter out=new PrintWriter( soc.getOutputStream () );
                    out.print ( "coutline " );
                    out.flush ();
                } catch (Exception e) {
                    e.printStackTrace ( );
                }
            }
        } ).start();
    }
    /**
     * @steps write();
     * @effect socket服务端发送信息
     * */
    public void sendMessage(final String chat)
    {
        Thread thread=new Thread(new Runnable( )
        {
            @Override
            public void run()
            {
                try {
                    PrintWriter out=new PrintWriter( socket.getOutputStream () );
                    out.print ( chat );
                    out.flush ();
                } catch (Exception e) {
                    e.printStackTrace ( );
                }
            }
        } );
        thread.start ();
    }

    /**
     * @steps read();
     * @effect socket服务端得到返回数据并发送到主界面
     * */
    public void returnMessage(String chat){
        Message msg=new Message();
        msg.obj=chat;
        serverHandler.sendMessage ( msg );
    }
    public void endListen(){
        isClint = false;

    }
    class ServerThread extends Thread {
        Socket socket;
        InputStream in;
        String str=null;
        boolean isClint=false;
        int endCount ;
        public ServerThread(Socket socket){
            super();
            this.socket = socket;
            try {
                this.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            isClint=true;
            try {

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String line = null;

                while ((line = reader.readLine())!=null) {//客户端的数据
                    Log.i("test","line = "+line);
                    if (line.equals("")){
                        break;
                    }
                }

                Log.i("edong","reader.close();");
                Resources res = App.getContext().getResources();
                Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
                    PrintStream writer = new PrintStream(socket.getOutputStream());
                    writer.println("HTTP/1.0 200 请求成功");// 返回应答消息,并结束应答
//                    writer.println("Content-Type:image/png");
//                    writer.println("Content-Disposition: inline; filename=\"ic_launcher.png\"");
//                    writer.println("Content-Length:" + bmp.getByteCount());// 返回内容字节数
                    writer.println();// 根据 HTTP 协议, 空行将结束头信息

//                    FileInputStream fis = new FileInputStream(fileToSend);
//                    byte[] buf = new byte[fis.available()];
//                    fis.read(buf);
//                    writer.write(buf);
//                StringBuffer stringBuffer = new StringBuffer("请求成功");
                writer.println("请求成功");
                int count = 0;
                while (count<5) {
                    sleep(5000);
                    writer.println("sleep(5000);\r\n"+count);
                    count++;
                }
                //Bitmap → byte[]
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                writer.write(baos.toByteArray());
                    writer.close();
                reader.close();
//                    fis.close();
                socket.close();
//                in =socket.getInputStream();
//                while (isClint&&!socket.isClosed()) {
//                    byte[] bt=new byte[size];
//                    in.read ( bt );
//                    str=new String( bt,"UTF-8" );//编码方式  解决收到数据乱码
//                    Log.i("test","msg = "+str);
//                    if (str!=null&&str!="exit") {
//                        if (str.contains("c")) {
//                            returnMessage ( str );
//                            endCount = 0;
//                        }else {
//                            endCount++;
//                            Log.i("count",endCount+"，"+str);
//                        }
//                        if (endCount>=1){
//                            Online.isOnline = false;
//                            isClint = false;
//                            sendMessage("你好");
//                        }
//                    }else if (str==null||str=="exit"){
//                        break;                                     //跳出循环结束socket数据接收
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace ( );
            }
        }
    }
}
