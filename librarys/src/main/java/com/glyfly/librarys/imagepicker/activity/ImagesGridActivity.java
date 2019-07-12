package com.glyfly.librarys.imagepicker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.glyfly.librarys.R;
import com.glyfly.librarys.imagepicker.AndroidImagePicker;
import com.glyfly.librarys.imagepicker.bean.ImageItem;
import com.glyfly.librarys.imagepicker.fragment.ImagesGridFragment;

public class ImagesGridActivity extends FragmentActivity implements View.OnClickListener, AndroidImagePicker.OnImageSelectedListener {
    private static final String TAG = ImagesGridActivity.class.getSimpleName();
    ImagesGridFragment mFragment;
    AndroidImagePicker androidImagePicker;
    String imagePath;
    private TextView mBtnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_activity_images_grid);

        androidImagePicker = AndroidImagePicker.getInstance();
        androidImagePicker.clearSelectedImages();//most of the time you need to clear the last selected images or you can comment out this line

        mBtnOk = (TextView) findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(this);

        if (androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_SINGLE) {
            mBtnOk.setVisibility(View.GONE);
        } else {
            mBtnOk.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.btn_backpress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imagePath = getIntent().getStringExtra(AndroidImagePicker.KEY_PIC_PATH);
        mFragment = new ImagesGridFragment();

        mFragment.setOnImageItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                position = androidImagePicker.isShouldShowCamera() ? position - 1 : position;

                if (androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_MULTI) {
                    go2Preview(position);
                } else if (androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_SINGLE) {
                    androidImagePicker.clearSelectedImages();
                    androidImagePicker.addSelectedImageItem(position, androidImagePicker.getImageItemsOfCurrentImageSet().get(position));
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();
        androidImagePicker.addOnImageSelectedListener(this);
        int selectedCount = androidImagePicker.getSelectImageCount();
        onImageSelected(0, null, selectedCount, androidImagePicker.getSelectLimit());
    }

    /**
     * 预览页面
     *
     * @param position
     */
    private void go2Preview(int position) {
        Intent intent = new Intent();
        intent.putExtra(AndroidImagePicker.KEY_PIC_SELECTED_POSITION, position);
        intent.setClass(ImagesGridActivity.this, ImagePreviewActivity.class);
        startActivityForResult(intent, AndroidImagePicker.REQ_PREVIEW);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_ok) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onImageSelected(int position, ImageItem item, int selectedItemsCount, int maxSelectLimit) {
        if (selectedItemsCount > 0) {
            mBtnOk.setEnabled(true);
            mBtnOk.setText(getResources().getString(R.string.select_complete, selectedItemsCount, maxSelectLimit));
        } else {
            mBtnOk.setText(getResources().getString(R.string.complete));
            mBtnOk.setEnabled(false);
        }
        Log.i(TAG, "=====EVENT:onImageSelected");
    }

    @Override
    protected void onDestroy() {
        androidImagePicker.removeOnImageItemSelectedListener(this);
        Log.i(TAG, "=====removeOnImageItemSelectedListener");
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == AndroidImagePicker.REQ_CAMERA) {
                Bitmap bmp = (Bitmap) data.getExtras().get("bitmap");
                Log.i(TAG, "=====get Bitmap:" + bmp.hashCode());
            } else if (requestCode == AndroidImagePicker.REQ_PREVIEW) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
