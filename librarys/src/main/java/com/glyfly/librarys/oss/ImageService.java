package com.glyfly.librarys.oss;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by OSS on 2015/12/11 0011.
 * 使用图片服务处理图片
 */
public class ImageService {
    private OssService ossService;
    //字体，默认文泉驿正黑，可以根据文档自行更改
    private static final String font = "d3F5LXplbmhlaQ==";

    public ImageService(OssService ossService) {
        this.ossService = ossService;
    }

    //给图片打上文字水印，除了大小字体之外其他都是默认值，有需要更改的可以参考图片服务文档自行调整
    public void textWatermark(String object, String text, int size) {
        String base64Text = Base64.encodeToString(text.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);

        String queryString = "@400w|watermark=2&type=" + font + "&text=" + base64Text + "&size=" + String.valueOf(size);
        Log.d("TextWatermark", object);
        Log.d("Text", text);
        Log.d("QuerySyring", queryString);

        ossService.asyncGetImage(object + queryString);
    }

    //强制缩放，其他缩放方式可以参考图片服务文档
    public void resize(String object, int width, int height) {
        String queryString = "@" + String.valueOf(width) + "w_" + String.valueOf(height) + "h_1e_1c";

        Log.d("ResizeImage", object);
        Log.d("Width", String.valueOf(width));
        Log.d("Height", String.valueOf(height));
        Log.d("QueryString", queryString);

        ossService.asyncGetImage(object + queryString);
    }

    //根据ImageView的大小自动缩放图片
    public static Bitmap autoResizeFromLocalFile(String picturePath, View view) throws IOException {
        if (view == null){
            return null;
        }
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);

        options.inSampleSize = calculateInSampleSize(options, view.getWidth(), view.getHeight());
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, options);
    }

    //根据ImageView大小自动缩放图片
    public static Bitmap autoResizeFromStream(InputStream stream, View view) throws IOException {

        if (view == null){
            return null;
        }
        byte[] data;
        {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = stream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            data = outStream.toByteArray();
        }

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, view.getWidth(), view.getHeight());
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }


    //计算图片缩放比例
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
