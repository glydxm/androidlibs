package com.glyfly.librarys.imagepicker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by Administrator on 2017/9/13.
 */

public class PermissionUtils {

    public static final int REQUEST_PERMISSION = 1;

    private static String[] PERMISSIONS_CAMERA_AND_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public static boolean isCameraPermission(Activity context, int requestCode){
        if (Build.VERSION.SDK_INT >= 23) {
            int storagePermission = ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if (storagePermission != PackageManager.PERMISSION_GRANTED || cameraPermission!= PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(context, PERMISSIONS_CAMERA_AND_STORAGE,
                        requestCode);
                return false;
            }
        }
        return true;
    }
}
