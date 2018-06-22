package com.zdd.myutil.net;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by yd on 2018/6/1.
 */

public class DownloadUtil {

    private static OkHttpClient okHttpClient;
    private static Call call;
    private static boolean isDownloading = false;
    private static List<DownPic> picList;
    public static void download(final String downUrl, final String savePath, final String fileName) {
        if (okHttpClient==null) {
            okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();
        }
        if (picList==null){
            picList = new ArrayList<>();
        }
        picList.add(new DownPic(downUrl,savePath,fileName));
        if (!isDownloading){
            download();
        }
    }
    private static void download(){
        if (picList!=null&&picList.size()>0){
            download(picList.get(0));
        }else {
            picList = null;
            okHttpClient = null;
            call = null;
        }
    }
    private static void download(final DownPic downPic){
        isDownloading = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request.Builder builder = new Request.Builder();
                builder.url(downPic.downUrl);
                builder.get();
                call = okHttpClient.newCall(builder.build());
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        download();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        BufferedSource source = response.body().source();
                        File outFile = new File(downPic.savePath+(downPic.savePath.endsWith(File.separator)?downPic.fileName: File.separator+downPic.fileName));
                        outFile.delete();
                        outFile.getParentFile().mkdirs();
                        outFile.createNewFile();

                        BufferedSink sink = Okio.buffer(Okio.sink(outFile));
                        source.readAll(sink);
                        sink.flush();
                        source.close();
                        picList.remove(0);
                        download();
                    }
                });
            }
        }).start();
    }
    static class DownPic{
        public String downUrl;
        public String savePath;
        public String fileName;
        public DownPic(String downUrl, String savePath, String fileName){
            this.downUrl = downUrl;
            this.savePath = savePath;
            this.fileName = fileName;
        }
    }
}
