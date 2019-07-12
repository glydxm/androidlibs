package com.glyfly.librarys.imagepicker.fragment;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.glyfly.librarys.imagepicker.widget.photodrawerview.OnPhotoTapListener;
import com.glyfly.librarys.imagepicker.widget.photodrawerview.PhotoDraweeView;

/**
 * Created by zhangfaming on 2017/9/7.
 */

public class SinglePreviewFragment extends Fragment {

    public static final String KEY_URL = "key_url";
    private PhotoDraweeView photoDraweeView;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        url = (String) bundle.getSerializable(KEY_URL);

        photoDraweeView = new PhotoDraweeView(getActivity());
        photoDraweeView.setBackgroundColor(0xff000000);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        photoDraweeView.setLayoutParams(params);

        photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().finish();
            }
        });
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "file://" + url;
        }

        ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(
                Uri.parse(url))
                .setResizeOptions(new ResizeOptions(768, 1280))
                .setAutoRotateEnabled(true);

        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setOldController(photoDraweeView.getController());
        controller.setImageRequest(requestBuilder.build());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        photoDraweeView.setController(controller.build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return photoDraweeView;
    }
}
