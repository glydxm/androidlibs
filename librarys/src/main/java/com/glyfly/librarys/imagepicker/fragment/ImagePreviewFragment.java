package com.glyfly.librarys.imagepicker.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.glyfly.librarys.R;
import com.glyfly.librarys.imagepicker.AndroidImagePicker;
import com.glyfly.librarys.imagepicker.bean.ImageItem;

import java.util.List;

public class ImagePreviewFragment extends Fragment {

    private static final String TAG = ImagePreviewFragment.class.getSimpleName();
    private Activity mContext;
    private ViewPager mViewPager;
    private TouchImageAdapter mAdapter;
    private List<ImageItem> mImageList;
    private int mCurrentItemPosition = 0;
    private boolean enableSingleTap = true;//singleTap to do something
    private AndroidImagePicker androidImagePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        androidImagePicker = AndroidImagePicker.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.picker_fragment_preview, null);
        mImageList = androidImagePicker.getImageItemsOfCurrentImageSet();
        mCurrentItemPosition = getArguments().getInt(AndroidImagePicker.KEY_PIC_SELECTED_POSITION, 0);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        mViewPager = (ViewPager) contentView.findViewById(R.id.viewpager);
        mAdapter = new TouchImageAdapter(((FragmentActivity) mContext).getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentItemPosition, false);
        ImageItem item = mImageList.get(mCurrentItemPosition);
        if (mContext instanceof OnImagePageSelectedListener) {
            boolean isSelected = false;
            if (androidImagePicker.isSelect(mCurrentItemPosition, item)) {
                isSelected = true;
            }
            ((OnImagePageSelectedListener) mContext).onImagePageSelected(mCurrentItemPosition, mImageList.get(mCurrentItemPosition), isSelected);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItemPosition = position;
                if (mContext instanceof OnImagePageSelectedListener) {
                    boolean isSelected = false;
                    ImageItem item = mImageList.get(mCurrentItemPosition);
                    if (androidImagePicker.isSelect(position, item)) {
                        isSelected = true;
                    }
                    ((OnImagePageSelectedListener) mContext).onImagePageSelected(mCurrentItemPosition, item, isSelected);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * public method:select the current show image
     */
    public void selectCurrent(boolean isCheck) {
        ImageItem item = mImageList.get(mCurrentItemPosition);
        boolean isSelect = androidImagePicker.isSelect(mCurrentItemPosition, item);
        if (isCheck) {
            if (!isSelect) {
                AndroidImagePicker.getInstance().addSelectedImageItem(mCurrentItemPosition, item);
            }
        } else {
            if (isSelect) {
                AndroidImagePicker.getInstance().deleteSelectedImageItem(mCurrentItemPosition, item);
            }
        }
    }

    class TouchImageAdapter extends FragmentStatePagerAdapter {

        public TouchImageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public Fragment getItem(int position) {
            SinglePreviewFragment fragment = new SinglePreviewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(SinglePreviewFragment.KEY_URL, mImageList.get(position).path);
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    /**
     * Interface for SingleTap Watching
     */
    public interface OnImageSingleTapClickListener {
        void onImageSingleTap(MotionEvent e);
    }

    /**
     * Interface for swipe page watching,you can get the current item,item position and whether the item is selected
     */
    public interface OnImagePageSelectedListener {
        void onImagePageSelected(int position, ImageItem item, boolean isSelected);
    }
}
