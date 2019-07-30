package com.glyfly.librarys.okhttp.callback;

import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/27.
 */

public abstract class StringCallBack extends Callback<String> {

    @Override
    public String parseNetworkResponse(Response response, int i) throws Exception {
        return response.body().string();
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        if (call.isCanceled()){
            return;
        }
    }
}
