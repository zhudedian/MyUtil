package com.zdd.andserver3.http;

import java.io.IOException;
import java.io.PrintStream;

public class HttpResponse {

    private PrintStream writer;

    public HttpResponse(PrintStream printStream){
        this.writer = printStream;
    }


    private void setStatusCode(int code){
        if (code<300){
            writer.println("HTTP/1.1 "+code+" OK");
        }else {
            writer.println("HTTP/1.1 "+code+" Not Found");
        }
    }

    public void setStatus(int code,String ... heads){
        setStatusCode(code);
        int size = heads.length;
        if (size>=2) {
            for (int i = 0; i < size; ) {
                writer.println(heads[i] + ": " + heads[i + 1]);
                i += 2;
            }
        }
        writer.println();
    }

    public void sendString(String out){
        writer.println(out);
    }

    public void write(byte[] bytes){
        try {
            writer.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
