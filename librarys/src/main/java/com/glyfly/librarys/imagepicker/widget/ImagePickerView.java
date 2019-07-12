package com.glyfly.librarys.imagepicker.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.glyfly.librarys.R;
import com.glyfly.librarys.imagepicker.AndroidImagePicker;
import com.glyfly.librarys.imagepicker.PermissionUtils;
import com.glyfly.librarys.imagepicker.Util;
import com.glyfly.librarys.imagepicker.activity.ImagesGridActivity;
import com.glyfly.librarys.imagepicker.activity.PreviewDelActivity;
import com.glyfly.librarys.imagepicker.bean.ImageItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImagePickerView extends NoScrollGrid {

    //图片选择数量
    private int maxImageSize = 9;
    //添加item布局
    private int noImgResource;
    //列选择数量
    private int columnNumber = 3;
    private Activity context;
    private ImagesAdapter adapter;
    private List<String> imageList;//图片选择list
    private static final int TYPE_SHOW_ADD = 0;
    private static final int TYPE_NO_SHOW_ADD = 1;
    private boolean isShowAdd = true;
    private int imageGridSize;

    public void setNoImgResource(int noImgResource) {
        this.noImgResource = noImgResource;
    }

    public void setMaxImageSize(int maxImageSize) {
        this.maxImageSize = maxImageSize;
    }

    public void setColumnNumber(int columnNumber) {
        if (columnNumber > 5) {
            columnNumber = 5;
        }
        this.columnNumber = columnNumber;
        this.setNumColumns(columnNumber);
    }

    public void setShowAdd(boolean showAdd) {
        isShowAdd = showAdd;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
        adapter.setImageList(imageList);
    }

    public List<String> getImageList() {
        return imageList;
    }

    public ImagePickerView(Context context) {
        this(context, null);
    }

    public ImagePickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 初始化ImagePickerView的一些信息
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ImagePickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = (Activity) context;
        adapter = new ImagesAdapter();
        this.setAdapter(adapter);
        if (imageList == null) {
            imageList = new ArrayList<>();
        }
        this.setNumColumns(columnNumber);
        this.setVerticalSpacing(10);
        this.setHorizontalSpacing(10);
        imageGridSize = (this.context.getWindowManager().getDefaultDisplay().getWidth() - Util.dp2px(context, 2) * 2) / columnNumber;
    }

    /**
     * 提供给外部调用用来再Activity返回时获取图片信息
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && !TextUtils.isEmpty(data.getStringExtra("photoPath"))) {//拍照
            imageList.add(data.getStringExtra("photoPath"));
        } else if (data != null && data.getSerializableExtra("images") != null) {//图片选择
            imageList = (List<String>) data.getSerializableExtra("images");
        } else {
            List<ImageItem> list = AndroidImagePicker.getInstance().getSelectedImages();
            for (int i = 0; i < list.size(); i++) {
                imageList.add(list.get(i).path);
            }
        }
        AndroidImagePicker.getInstance().setSelectLimit(maxImageSize - imageList.size());
        adapter.setImageList(imageList);
    }

    class ImagesAdapter extends BaseAdapter {

        List<String> imageList;

        public ImagesAdapter() {
            this.imageList = new ArrayList();
        }

        public void setImageList(List<String> imageList) {
            this.imageList = imageList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (isShowAdd) {
                if (imageList == null || imageList.isEmpty()) {
                    return 1;
                }
                if (imageList.size() >= maxImageSize) {
                    return maxImageSize;
                }
                return imageList.size() + 1;
            }
            if (imageList.size() >= maxImageSize) {
                return maxImageSize;
            }
            return imageList.size() + 1;
        }

        @Override
        public String getItem(int position) {
            if (isShowAdd) {
                if (position == imageList.size()) {
                    return null;
                }
                return imageList.get(position - 1);
            }
            return imageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (isShowAdd) {
                return position == imageList.size() ? TYPE_SHOW_ADD : TYPE_NO_SHOW_ADD;
            } else {
                return TYPE_NO_SHOW_ADD;
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int itemViewType = getItemViewType(position);
            if (itemViewType == TYPE_SHOW_ADD) {
                if (noImgResource != 0) {//加载用户的添加item布局
                    convertView = LayoutInflater.from(context).inflate(noImgResource, parent, false);
                } else {//默认的添加item布局
                    convertView = LayoutInflater.from(context).inflate(R.layout.picker_grid_item_camera, parent, false);
                }
                convertView.setTag(null);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (PermissionUtils.isCameraPermission(context, PermissionUtils.REQUEST_PERMISSION)) {
                            Intent intent = new Intent(context, ImagesGridActivity.class);
                            Activity activity = context;
                            activity.startActivityForResult(intent, 1001);
                        }
                    }
                });
                TextView grid_add = convertView.findViewById(R.id.grid_add);
                grid_add.setText("添加图片");
                Drawable drawable = getResources().getDrawable(R.drawable.picker_ic_add);
                grid_add.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            } else {
                final ViewHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.picker_image_grid_item, null);
                    holder = new ViewHolder();
                    holder.ivPic = (SimpleDraweeView) convertView.findViewById(R.id.iv_thumb);
                    holder.cbPanel = convertView.findViewById(R.id.thumb_check_panel);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PreviewDelActivity.class);
                        intent.putExtra("images", (Serializable) imageList);
                        intent.putExtra("position", position);
                        context.startActivityForResult(intent, 1002);
                    }
                });
                ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(
                        Uri.parse(String.format("file://%s", imageList.get(position))))
                        .setResizeOptions(new ResizeOptions(imageGridSize, imageGridSize))
                        .setAutoRotateEnabled(true);
                PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setOldController(holder.ivPic.getController())
                        .setImageRequest(requestBuilder.build())
                        .build();
                holder.ivPic.setController(controller);
            }
            return convertView;
        }
    }

    class ViewHolder {
        SimpleDraweeView ivPic;
        View cbPanel;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == PermissionUtils.REQUEST_PERMISSION){
            Intent intent = new Intent(context, ImagesGridActivity.class);
            Activity activity = context;
            activity.startActivityForResult(intent, 1001);
        }
    }
}
