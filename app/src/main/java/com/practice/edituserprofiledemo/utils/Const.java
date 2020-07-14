package com.practice.edituserprofiledemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Const {


    public static MultipartBody.Part bitmapConvertToFile(Context context, Bitmap bitmap, int type) {
        // 비트맵 -> 파일로
        FileOutputStream fileOutputStream = null;
        File bitmapFile = null;
        try {
            File file = null;
            file = new File(context.getCacheDir(), "");

            // File file = new File("/sdcard/cropTest/", "");
            if (!file.exists()) {
                file.mkdir();
            }

            String fileName = System.currentTimeMillis() + ".jpg";
            bitmapFile = new File(file, fileName);

            fileOutputStream = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            MediaScannerConnection.scanFile(context, new String[]{bitmapFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {

                }

                @Override
                public void onScanCompleted(String path, Uri uri) {
                    //runOnUiThread(() -> Toast.makeText(MainActivity.this, "file saved", Toast.LENGTH_LONG).show());
                }
            });
            // file.delete();
            return Const.prepareFilePart(bitmapFile, type);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    @NonNull
    public static MultipartBody.Part prepareFilePart(File file, int type) {
        try {
            String MULTIPART_FORM_DATA = "image/jpg";
            String fileName = file.getName();
            int size = fileName.split("\\.").length;
            String extension = fileName.split("\\.")[size - 1];
            if (isNotNullEmpty(fileName) && file.getName().split("\\.")[1].contains("gif") || file.getName().split("\\.")[1].contains("GIF")) {
                MULTIPART_FORM_DATA = "image/gif";
            }
            RequestBody requestFile = RequestBody.create(file, MediaType.parse(MULTIPART_FORM_DATA));
            return MultipartBody.Part.createFormData("files", type + "." + extension, requestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNotNullEmpty(String... msg) {
        for (String str : msg) {
            if (str == null || str.isEmpty()) {
                return false;
            }
        }
        return true;
    }


    public static Bitmap resizeThumnail(Bitmap bitmap, int width, int height) {

        if (width > 640 || height > 640) {
            if (width > height) { //가로가 길면
                height *= (640F / width);
                width = (int) 640F;

            } else { //세로가 길면
                width *= (640F / height);
                height = (int) 640F;
            }
        }
        Bitmap convertedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        if (convertedBitmap.getRowBytes() > bitmap.getRowBytes()) {
            return bitmap;
        } else {
            return convertedBitmap;
        }
    }
}
