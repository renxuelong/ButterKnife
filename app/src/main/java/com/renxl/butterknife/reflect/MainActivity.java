package com.renxl.butterknife.reflect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.renxl.butterknife.R;
import com.renxl.butterknife.compile.ClassAnnotationActivity;

/**
 * 运行时注解测试 Activity
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_reflect, R.id.btn_annotation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reflect:
                startActivity(new Intent(this, ReflectActivity.class));
                break;
            case R.id.btn_annotation:
                startActivity(new Intent(this, ClassAnnotationActivity.class));
                break;
        }
    }

}
