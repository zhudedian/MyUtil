package com.zdd.andserver3.thread;



import com.zdd.andserver3.inter.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class CoreThread extends Thread {

    private final int mPort;
    private final int mTimeout;
    private final Map<String, RequestHandler> mHandlerMap;
    
    private boolean isLoop;
    private ServerSocket mServerSocket;

    public CoreThread(int port, int timeout, Map<String, RequestHandler> handlerMap) {
        this.mPort = port;
        this.mTimeout = timeout;
        this.mHandlerMap = handlerMap;
    }




    @Override
    public void run() {

        try {
            this.mServerSocket = new ServerSocket(this.mPort);
//            this.mServerSocket.setReuseAddress(true);

//            try {
//                this.mServerSocket.bind(new InetSocketAddress(this.mPort));
//            } catch (final IOException var12) {
//
//                return;
//            }
            this.isLoop = true;

            while(this.isLoop) {
                if (!this.mServerSocket.isClosed()) {
                    Socket socket = this.mServerSocket.accept();
                    new SocketThread(socket,mHandlerMap).start();
                }
            }

        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            this.shutdown();
        }
    }

    void shutdown() {
        this.isLoop = false;

        try {
            if (this.mServerSocket != null) {
                this.mServerSocket.close();
            }
        } catch (IOException var2) {

        }

        if (this.isInterrupted()) {
            this.interrupt();
        }



    }

    boolean isRunning() {
        return this.isLoop;
    }
    
    
}
