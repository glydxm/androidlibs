package com.glyfly.librarys.zxing;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.glyfly.librarys.R;


public class ZxingResultActivity extends Activity {

    private TextView txt_title;
    private TextView tv_webview_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxing_activity_result);
        initView();
        if (getIntent().getExtras() != null && !TextUtils.isEmpty(getIntent().getExtras().getString("data"))) {
            txt_title.setText(getIntent().getExtras().getString("data"));
        }
    }

    private void initView() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        tv_webview_back = (TextView) findViewById(R.id.tv_webview_back);
        tv_webview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
