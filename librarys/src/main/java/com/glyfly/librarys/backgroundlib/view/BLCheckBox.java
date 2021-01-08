package com.glyfly.librarys.backgroundlib.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.glyfly.librarys.backgroundlib.BackgroundFactory;


public class BLCheckBox extends AppCompatCheckBox {
    public BLCheckBox(Context context) {
        super(context);
    }

    public BLCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BLCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        BackgroundFactory.setViewBackground(context, attrs, this);
    }
}
