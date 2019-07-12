package com.glyfly.librarys.imagepicker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.glyfly.librarys.R;
import com.glyfly.librarys.imagepicker.AndroidImagePicker;
import com.glyfly.librarys.imagepicker.fragment.SinglePreviewFragment;

import java.io.Serializable;
import java.util.List;


/**
 * 图片预览删除
 *
 */
public class PreviewDelActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ImagePreviewActivity.class.getSimpleName();
    private TextView mTitleCount;
    private TextView mBtnOk;
    private ImageView backBtn;
    private List<String> mImageList;
    private int mShowItemPosition = 0;
    private ViewPager mViewPager;
    private TouchImageAdapter mAdapter ;
    private boolean enableSingleTap = true;//singleTap to do something

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_activity_preview_del);

        mImageList = (List<String>) getIntent().getSerializableExtra("images");
        mShowItemPosition = getIntent().getIntExtra("position",0);

        mBtnOk = (TextView) findViewById(R.id.btn_del);
        backBtn = (ImageView) findViewById(R.id.btn_backpress);
        mBtnOk.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        mTitleCount = (TextView) findViewById(R.id.tv_title_count);
        mTitleCount.setText(mShowItemPosition+1+"/" + mImageList.size());

        initView();

        AndroidImagePicker.getInstance().clearSelectedImages();
    }

    private void initView() {
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mAdapter = new TouchImageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mShowItemPosition, false);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitleCount.setText(position+1+"/" + mImageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_del) {
            mAdapter.remove(mViewPager.getCurrentItem());
            mTitleCount.setText(mViewPager.getCurrentItem()+1+"/" + mImageList.size());
            if (mImageList.size()==0){
                Intent intent = new Intent();
                intent.putExtra("images", (Serializable) mImageList);
                setResult(RESULT_OK,intent);
                finish();
            }
        }else if (i==R.id.btn_backpress){
            Intent intent = new Intent();
            intent.putExtra("images", (Serializable) mImageList);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.putExtra("images", (Serializable) mImageList);
            setResult(RESULT_OK,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class TouchImageAdapter extends FragmentStatePagerAdapter {
        public TouchImageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        public void remove(int position){
            mImageList.remove(position);
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
           SinglePreviewFragment fragment = new SinglePreviewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(SinglePreviewFragment.KEY_URL, mImageList.get(position));
            fragment.setArguments(bundle);
            return fragment;
        }
    }
}
