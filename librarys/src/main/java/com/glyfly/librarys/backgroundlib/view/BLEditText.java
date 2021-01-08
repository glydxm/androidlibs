package com.glyfly.librarys.backgroundlib.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.glyfly.librarys.backgroundlib.BackgroundFactory;


public class BLEditText extends AppCompatEditText {
    public BLEditText(Context context) {
        super(context);
    }

    public BLEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BLEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        BackgroundFactory.setViewBackground(context, attrs, this);
    }
}
