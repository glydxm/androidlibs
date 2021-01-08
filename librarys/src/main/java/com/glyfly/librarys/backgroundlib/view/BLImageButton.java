package com.glyfly.librarys.backgroundlib.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageButton;

import com.glyfly.librarys.backgroundlib.BackgroundFactory;


public class BLImageButton extends AppCompatImageButton {
    public BLImageButton(Context context) {
        super(context);
    }

    public BLImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BLImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        BackgroundFactory.setViewBackground(context, attrs, this);
    }
}
