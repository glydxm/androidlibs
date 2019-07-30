package com.glyfly.librarys.okhttp.tool;

import android.content.Context;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HttpTool {

	private static HttpTool httpTool;
	private HttpTool(){}

	private static Map<String, String> headers = new HashMap<>();

	public static HttpTool getInstance() {
		if (httpTool == null) {
			httpTool = new HttpTool();
		}
		return httpTool;
	}

	public static void initHeaders(Map<String, String> header) {
		if (header != null) {
			headers.putAll(header);
		}
	}

	/**
	 * 模拟GET表单（无参数）
	 *
	 * @param url
	 *            请求URL
	 * @param callback
	 *            回调函数（可根据自己需求自定义）
	 */
	public static <T> void get(Context context, String url, Callback<T> callback) {
		OkHttpUtils.get()//
				.url(url)//
				.tag(context)
				.headers(headers)
				.build()//
				.execute(callback);
	}

	/**
	 * 模拟GET表单（有参数）
	 *
	 * @param url
	 *            请求URL
	 * @param params
	 *            参数
	 * @param callback
	 *            回调函数（可根据自己需求自定义）
	 */
	public static <T> void get(Context context, String url, Map<String, String> params,
							   Callback<T> callback) {
		OkHttpUtils.get()//
				.url(url)//
				.tag(context)
				.params(params)//
				.headers(headers)
				.build()//
				.execute(callback);
	}

	/**
	 * 模拟GET表单（有参数）
	 *
	 * @param url
	 *            请求URL
	 * @param params
	 *            参数
	 * @param callback
	 *            回调函数（可根据自己需求自定义）
	 */
	public static <T> void getNoHeaders(Context context, String url, Map<String, String> params,
							   Callback<T> callback) {
		OkHttpUtils.get()//
				.url(url)//
				.tag(context)
				.params(params)//
				.build()//
				.execute(callback);
	}

	/**
	 * 模拟GET表单（有参数）
	 *
	 * @param url
	 *            请求URL
	 * @param params
	 *            参数
	 * @param callback
	 *            回调函数（可根据自己需求自定义）
	 */
	public static <T> void get(Context context, String url, Map<String, String> params,
							   Map<String, String> header, Callback<T> callback) {
		if (header == null){
			header = new HashMap<>();
		}
		headers.putAll(header);
		OkHttpUtils.get()//
				.url(url)//
				.tag(context)
				.params(params)//
				.headers(headers)
				.build()//
				.execute(callback);
	}

	/**
	 * post请求（无参数）
	 *
	 * @param context
	 *            上下文
	 * @param url
	 *            请求URL
	 * @param callback
	 *            回调函数 （可根据自己需求自定义）
	 */
	public static <T> void post(Context context, String url, Callback<T> callback) {
		OkHttpUtils.post()//
				.url(url)//
				.tag(context)//
				.headers(headers)//
				.build()//
				.execute(callback);
	}

	/**
	 * post请求（有参数）
	 *
	 * @param context
	 *            上下文
	 * @param url
	 *            请求URL
	 * @param params
	 *            参数
	 * @param callback
	 *            回调函数（可根据自己需求自定义）
	 */
	public static <T> void post(Context context, String url, Map<String, String> params,
								Callback<T> callback) {
		OkHttpUtils.post()//
				.url(url)//
				.tag(context)//
				.headers(headers)//
				.params(params)//
				.build()//
				.execute(callback);
	}


	/**
	 * post方式,单个文件上传（无参数）
	 *
	 * @param context
	 *            上下文
	 * @param url
	 *            上传URL
	 * @param key
	 *            上传文件key
	 * @param file
	 *            上传文件
	 * @param callback
	 *            回调函数
	 */
	public static <T> void updateFile(Context context, String url, String key, File file,
									  Callback<T> callback) {
		OkHttpUtils.post()//
				.addFile(key, file.getName(), file)//
				.tag(context)
				.url(url)//
				.headers(headers)//
				.build()//
				.execute(callback);
	}

	/**
	 * post方式,单个文件上传（有参数）
	 *
	 * @param context
	 *            上下文
	 * @param url
	 *            上传URL
	 * @param keys
	 *            上传文件key数组
	 * @param file
	 *            上传文件
	 * @param params
	 *            参数
	 * @param callback
	 *            回调函数
	 */
	public static <T> void updateFile(Context context, String url, String keys, File file,
									  Map<String, String> params, Callback<T> callback) {
		OkHttpUtils.post()//
				.addFile(keys, file.getName(), file)//
				.url(url)//
				.tag(context)
				.params(params)//
				.headers(headers)//
				.build()//
				.execute(callback);
	}

	/**
	 * post方式,多个文件上传
	 *
	 * @param context
	 *            上下文
	 * @param url
	 *            上传URL
	 * @param keys
	 *            上传文件key数组
	 * @param files
	 *            上传文件数组
	 * @param callback
	 *            回调函数
	 */

	public static <T> void updateFile(Context context, String url,
									  String[] keys, File[] files, Callback<T> callback) {
		PostFormBuilder Builder = OkHttpUtils.post();
		for (int i = 0; i < files.length; i++) {
			Builder.addFile(keys[i], files[i].getName(), files[i]);
		}
		Builder.url(url)//
				.tag(context)
				.headers(headers)
				.build()//
				.execute(callback);
	}

}
