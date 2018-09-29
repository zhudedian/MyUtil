package com.zdd.andserver3;

import com.zdd.andserver3.inter.RequestHandler;
import com.zdd.andserver3.thread.CoreThread;

import java.util.HashMap;
import java.util.Map;

public class AndServer {

    private final int mPort;
    private final int mTimeout;
    private final Map<String, RequestHandler> mHandlerMap;

    private AndServer(AndServer.Build build) {
        this.mPort = build.mPort;
        this.mTimeout = build.mTimeout;
        this.mHandlerMap = build.mHandlerMap;

    }

    public Thread createServer() {
        return new CoreThread(this.mPort, this.mTimeout, this.mHandlerMap);
    }

    public static final class Build {
        private int mPort = 8080;
        private int mTimeout = 10000;
        private Map<String, RequestHandler> mHandlerMap = new HashMap(2);

        public Build() {
        }

        public AndServer.Build port(int mPort) {
            this.mPort = mPort;
            return this;
        }

        public AndServer.Build timeout(int mTimeout) {
            this.mTimeout = mTimeout;
            return this;
        }

        public AndServer.Build registerHandler(String name, RequestHandler handler) {
            this.mHandlerMap.put(name, handler);
            return this;
        }



        public AndServer build() {
            return new AndServer(this);
        }
    }
}
