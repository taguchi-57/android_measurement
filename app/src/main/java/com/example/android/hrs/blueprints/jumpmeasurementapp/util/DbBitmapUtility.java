package com.example.android.hrs.blueprints.jumpmeasurementapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

// DBに画像を保存するためのUtil
// https://riptutorial.com/ja/android/example/13308/sqlite%E3%81%AB%E7%94%BB%E5%83%8F%E3%82%92%E4%BF%9D%E5%AD%98%E3%81%99%E3%82%8B
public class DbBitmapUtility {

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}
