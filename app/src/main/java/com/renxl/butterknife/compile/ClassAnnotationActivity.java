package com.renxl.butterknife.compile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.annotation.ViewInjector;
import com.renxl.butterknife.R;

/**
 * 编译时注解测试 Activity
 */
public class ClassAnnotationActivity extends AppCompatActivity {

    @ViewInjector(R.id.textView)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_annotation);
        com.renxl.butterknife.compile.ButterKnife.bind(this);

        tv.setText("Hello Compile Annotation !!!");
    }

}
