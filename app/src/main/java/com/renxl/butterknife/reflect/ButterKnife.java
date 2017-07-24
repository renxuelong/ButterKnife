package com.renxl.butterknife.reflect;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by renxl
 * On 2017/7/9 11:51.
 * <p>
 * 运行时注解
 */

public class ButterKnife {
    public static void bind(Activity activity) {
        bindField(activity);
        bindMethod(activity);
    }

    private static void bindMethod(final Activity activity) {
        // 获取所有方法
        Method[] methods = activity.getClass().getDeclaredMethods();
        // 遍历所有方法
        for (final Method method : methods) {
            method.setAccessible(true);
            // 找到使用了 OnClick 注解的方法
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                // 获取注解中的值
                int[] ids = onClick.value();
                // 遍历所有值
                for (int id : ids) {
                    // 根据值找到对应元素，并设置监听器，在被点击的时候通过反射调用注解方法
                    View view = activity.findViewById(id);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                method.invoke(activity, v);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }

    private static void bindField(Activity activity) {
        Field[] fields = activity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView != null) {
                int id = bindView.value();

                View view = activity.findViewById(id);
                if (view != null) {
                    try {
                        field.set(activity, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
