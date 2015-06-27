package com.aslam.zeshan.ocr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;

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

    String ID;

    ProgressDialog progressDialog;

    public PostAPI(Context con, File file) {
        this.con = con;
        this.file = file;
    }

    public void uploadFile() throws IOException {
        progressDialog = new ProgressDialog(con);
        progressDialog.setTitle("OCR");
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("file", file);

        client.post("http://api.newocr.com/v1/upload?key=fdfa2801261915bb84f9e9379f740f6f", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes, StandardCharsets.UTF_8);
                ID = new JSONUtil().getFileID(str);

                try {
                    progressDialog.setMessage("Getting Text...");
                    progressDialog.setProgress(75);
                    
                    getText();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                progressDialog.setMessage("Failed!");
                progressDialog.setProgress(0);
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                int p = 100 * bytesWritten / totalSize;

                progressDialog.setProgress(p / 50);
            }
        });
    }

    public void getText() throws IOException {
        System.out.println(ID);
        AsyncHttpClient client = new AsyncHttpClient();

        String URL = "http://api.newocr.com/v1/ocr?key=fdfa2801261915bb84f9e9379f740f6f&file_id=" + ID + "&page=1&lang=eng&psm=6";
        client.get(URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes, StandardCharsets.UTF_8);

                TextView textView = (TextView) ((Activity) con).findViewById(R.id.textView);
                textView.setText(new JSONUtil().getText(str));

                progressDialog.setProgress(100);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String str = new String(bytes, StandardCharsets.UTF_8);

                progressDialog.setMessage("Failed! " + str);
                progressDialog.setProgress(0);
            }
        });
    }
}
