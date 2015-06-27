package com.aslam.zeshan.ocr;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PostAPI {

    Context con;
    File file;

    public PostAPI(Context con, File file) {
        this.con = con;
        this.file = file;
    }

    public void uploadFile() throws IOException {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("file", file);

        client.post("http://api.newocr.com/v1/upload?key=fdfa2801261915bb84f9e9379f740f6f", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("Testing 1: " + str);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String str = new String(bytes, StandardCharsets.UTF_8);
                System.out.println(str);
            }
        });
    }
}
