package com.zdd.andserver3.http;

import java.util.ArrayList;
import java.util.List;

public class HttpRequest {

    private List<Header> headers;


    public HttpRequest(){
        this.headers = new ArrayList<>();
    }

    public void addHeader(String name, String value){
        headers.add(new Header(name,value));
    }

    public List<Header> getHeaders(String name){
        List<Header> headerList = new ArrayList<>();
        for (int i=0;i<headers.size();i++){
            Header header = headers.get(i);
            if (header.getName().equals(name)){
                headerList.add(header);
            }
        }
        return headerList;
    }


    public Header getFirstHeader(String name){
        for (int i=0;i<headers.size();i++){
            Header header = headers.get(i);
            if (header.getName().equals(name)){
                return header;
            }
        }
        return new Header(name,"");
    }

    public Header getLastHeader(String name){
        for (int i=headers.size()-1;i>=0;i--){
            Header header = headers.get(i);
            if (header.getName().equals(name)){
                return header;
            }
        }
        return new Header(name,"");
    }

    public List<Header> getAllHeaders(){
        return headers;
    }

    public boolean containsHeader(String name){
        for (Header header:headers){
            if (header.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

}
