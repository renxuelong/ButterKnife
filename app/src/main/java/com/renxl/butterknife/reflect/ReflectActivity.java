package com.renxl.butterknife.reflect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.renxl.butterknife.R;

public class ReflectActivity extends AppCompatActivity {

    @BindView(R.id.tv_text)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflect);
        ButterKnife.bind(this);
        mTextView.setText("Hello ButterKnife");
    }
}
