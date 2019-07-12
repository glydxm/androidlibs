package com.glyfly.librarys.oss;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by oss on 2015/12/7 0007.
 * 支持普通上传，普通下载和断点上传
 */
public class OssService {

    private Activity activity;
    private OSSClient ossClient;
    private String bucket;
    private LoadListener loadListener;
    private MultiPartUploadManager multiPartUploadManager;
    private String callbackAddress;
    //根据实际需求改变分片大小
    private final static int partSize = 256 * 1024;

    public static final int UPLOADING = 1;
    public static final int UPLOAD_COMPLETE = 2;
    public static final int UPLOAD_FAILED = 3;
    public static final int DOWNLOADING = 4;
    public static final int DOWNLOAD_COMPLETE = 5;
    public static final int DOWNLOAD_FAILED = 6;

    public interface LoadListener {
        void loadProgress(int progress);
        void loadComplete(Object object);
        void loadFail(String error);
    }

    private WeakHandler handler = new WeakHandler(activity){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPLOADING:
                    if (msg.obj instanceof Integer) {
                        int progress = (int) msg.obj;
                        if (loadListener != null) {
                            loadListener.loadProgress(progress);
                        }
                    }
                    break;
                case UPLOAD_COMPLETE:
                    if (loadListener != null) {
                        loadListener.loadComplete(msg.obj);
                    }
                    break;
                case UPLOAD_FAILED:
                    if (msg.obj instanceof String) {
                        String error = (String) msg.obj;
                        if (loadListener != null) {
                            loadListener.loadFail(error);
                        }
                    }
                    break;
                case DOWNLOADING:
                    if (msg.obj instanceof Integer) {
                        int progress = (int)msg.obj;
                        if (loadListener != null) {
                            loadListener.loadProgress(progress);
                        }
                    }
                    break;
                case DOWNLOAD_COMPLETE:
                    if (loadListener != null) {
                        loadListener.loadComplete(msg.obj);
                    }
                    break;
                case DOWNLOAD_FAILED:
                    if (msg.obj instanceof String) {
                        String error = (String)msg.obj;
                        if (loadListener != null) {
                            loadListener.loadFail(error);
                        }
                    }
                    break;
            }
        }
    };

    public OssService(Activity activity, OSSClient ossClient, String bucket) {
        this.activity = activity;
        this.ossClient = ossClient;
        this.bucket = bucket;
        this.multiPartUploadManager = new MultiPartUploadManager(ossClient, bucket, partSize, null);
    }

    public OssService(Activity activity, OSSClient ossClient, String bucket, LoadListener loadListener) {
        this.activity = activity;
        this.ossClient = ossClient;
        this.bucket = bucket;
        this.loadListener = loadListener;
        this.multiPartUploadManager = new MultiPartUploadManager(ossClient, bucket, partSize, handler);
    }

    public void setLoadListener(LoadListener loadListener){
        this.loadListener = loadListener;
    }

    public void setBucketName(String bucket) {
        this.bucket = bucket;
    }

    public void setCallbackAddress(String callbackAddress) {
        this.callbackAddress = callbackAddress;
    }

    public void asyncGetImage(String object) {
        if ((object == null) || object.equals("")) {
            Log.w("AsyncGetImage", "ObjectNull");
            return;
        }

        GetObjectRequest get = new GetObjectRequest(bucket, object);

        OSSAsyncTask task = ossClient.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                InputStream inputStream = result.getObjectContent();
                //重载InputStream来获取读取进度信息
                ProgressInputStream progressStream = new ProgressInputStream(inputStream, new OSSProgressCallback<GetObjectRequest>() {
                    @Override
                    public void onProgress(GetObjectRequest o, long currentSize, long totalSize) {
                        Log.d("GetObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                        int progress = (int) (100 * currentSize / totalSize);
                        handler.obtainMessage(OssService.DOWNLOADING, progress).sendToTarget();
                    }
                }, result.getContentLength());
                Bitmap bitmap = BitmapFactory.decodeStream(progressStream);
                handler.obtainMessage(OssService.DOWNLOAD_COMPLETE, bitmap).sendToTarget();
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                String info = "";
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    info = clientExcepion.toString();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    info = serviceException.toString();
                }
                handler.obtainMessage(OssService.DOWNLOAD_FAILED, info).sendToTarget();
            }
        });
    }

    public void asyncPutImage(String object, String localFile) {
        if (object.equals("")) {
            object = System.currentTimeMillis()/1000 + localFile.substring(localFile.lastIndexOf("."));
        }

        File file = new File(localFile);
        if (!file.exists()) {
            Log.w("AsyncPutImage", "FileNotExist");
            Log.w("LocalFile", localFile);
            return;
        }

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, object, localFile);

        if (callbackAddress != null) {
            // 传入对应的上传回调参数，这里默认使用OSS提供的公共测试回调服务器地址
            put.setCallbackParam(new HashMap<String, String>() {
                {
                    put("callbackUrl", callbackAddress);
                    //callbackBody可以自定义传入的信息
                    put("callbackBody", "filename=${object}");
                }
            });
        }

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                int progress = (int) (100 * currentSize / totalSize);
                handler.obtainMessage(OssService.UPLOADING, progress).sendToTarget();
            }
        });

        OSSAsyncTask task = ossClient.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                String object = request.getObjectKey();
                handler.obtainMessage(OssService.UPLOAD_COMPLETE, object).sendToTarget();
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                String info = "";
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    info = clientExcepion.toString();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    info = serviceException.toString();
                }
                handler.obtainMessage(OssService.UPLOAD_FAILED, info).sendToTarget();
            }
        });
    }

    //断点上传，返回的task可以用来暂停任务
    public PauseableUploadTask asyncMultiPartUpload(String object, String localFile) {
        if (object.equals("")) {
            Log.w("AsyncMultiPartUpload", "ObjectNull");
            return null;
        }

        File file = new File(localFile);
        if (!file.exists()) {
            Log.w("AsyncMultiPartUpload", "FileNotExist");
            Log.w("LocalFile", localFile);
            return null;
        }

        Log.d("MultiPartUpload", localFile);
        PauseableUploadTask task = multiPartUploadManager.asyncUpload(object, localFile);
        return task;
    }

    public String getImageUrl(String object){
        return ossClient.presignPublicObjectURL("highlife-picture", object);
    }
}
