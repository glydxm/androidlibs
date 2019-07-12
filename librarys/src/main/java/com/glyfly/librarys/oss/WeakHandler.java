package com.glyfly.librarys.oss;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/9/14.
 */

public class WeakHandler extends Handler {

    private final WeakReference<Activity> activity;

    public WeakHandler(Activity activity) {
        super(Looper.getMainLooper());
        this.activity = new WeakReference<Activity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (activity.get() == null || activity.get().isFinishing()){
            return;
        }
    }
}
