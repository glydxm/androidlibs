package com.glyfly.librarys.imagepicker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class NoScrollGrid extends GridView {

    public NoScrollGrid(Context context) {
        super(context);
    }

    public NoScrollGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}