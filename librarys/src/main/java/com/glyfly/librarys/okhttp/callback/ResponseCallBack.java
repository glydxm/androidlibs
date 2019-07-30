package com.glyfly.librarys.okhttp.callback;

import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import okhttp.model.ResponseModel;
import okhttp3.Response;

public abstract class ResponseCallBack extends Callback<ResponseModel> {

	@Override
	public ResponseModel parseNetworkResponse(Response arg0, int arg1)
			throws Exception {
		ResponseModel response=new ResponseModel();
		response.setBady(new JSONObject(arg0.body().string()));
		response.setHeader(arg0.headers());
		return response;
	}
}
