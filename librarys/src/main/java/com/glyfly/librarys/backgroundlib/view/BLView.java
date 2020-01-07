package com.glyfly.librarys.backgroundlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.glyfly.librarys.backgroundlib.BackgroundFactory;


public class BLView extends View {
    public BLView(Context context) {
        super(context);
    }

    public BLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BLView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        BackgroundFactory.setViewBackground(context, attrs, this);
    }
}
