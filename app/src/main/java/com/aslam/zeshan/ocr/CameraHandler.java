package com.aslam.zeshan.ocr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

public class CameraHandler {

    Context con;

    public CameraHandler(Context con) {
        this.con = con;
    }

    public void takePic() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        ((Activity)con).startActivityForResult(intent, 1);
    }
}
