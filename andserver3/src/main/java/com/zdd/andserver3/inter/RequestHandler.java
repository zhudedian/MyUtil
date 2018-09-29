package com.zdd.andserver3.inter;

import com.zdd.andserver3.http.HttpRequest;
import com.zdd.andserver3.http.HttpResponse;


public interface RequestHandler {

    void handle(HttpRequest request,HttpResponse response);
}
