package com.glyfly.librarys.backgroundlib.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.glyfly.librarys.backgroundlib.BackgroundFactory;


public class BLConstraintLayout extends ConstraintLayout {
    public BLConstraintLayout(Context context) {
        super(context);
    }

    public BLConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BLConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        BackgroundFactory.setViewBackground(context, attrs, this);
    }
}
