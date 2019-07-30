package com.glyfly.librarys.okhttp.callback;

import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;

import okhttp3.Response;

public abstract class JSONArrayCallBack extends Callback<JSONArray> {

	@Override
	public JSONArray parseNetworkResponse(Response arg0, int arg1)
			throws Exception {
		return new JSONArray(arg0.body().string());
	}
}
