package com.zdd.andserver3.thread;



import android.util.Log;

import com.zdd.andserver3.http.HttpRequest;
import com.zdd.andserver3.http.HttpResponse;
import com.zdd.andserver3.inter.RequestHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;

public class SocketThread extends Thread {

    Socket socket;

    private final Map<String, RequestHandler> mHandlerMap;



    public SocketThread(Socket socket, Map<String, RequestHandler> handlerMap){
        this.socket = socket;
        this.mHandlerMap = handlerMap;
    }

    @Override
    public void run() {
        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String line = reader.readLine();
//            Log.i("test","line = "+line);
            String[] urls = line.split(" ");
            String name = urls[1].split("\\?")[0].replace("/","");
            RequestHandler requestHandler = mHandlerMap.get(name);
            if (requestHandler!=null){
                HttpRequest request = new HttpRequest();
                while ((line = reader.readLine())!=null) {//客户端的数据
//                    Log.i("test","line = "+line);
                    if (!line.equals("")){
                        String[] headers = line.split(": ");
                        request.addHeader(headers[0],headers[1]);
                    }else {
                        break;
                    }
                }
                request.setReader(reader);

                PrintStream writer = new PrintStream(socket.getOutputStream());
                HttpResponse response = new HttpResponse(writer);
                requestHandler.handle(request,response);
                writer.close();
            }



            reader.close();

            socket.close();

        } catch (Exception e) {
            e.printStackTrace ( );
        }
    }
}
