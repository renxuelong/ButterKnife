package com.renxl.butterknife.compile;

import android.app.Activity;

import com.annotation.adapter.InjectAdapter;

/**
 * Created by renxl
 * On 2017/7/22 17:21.
 */

public class ButterKnife {

    /**
     * 生成的辅助类的同一后缀
     */
    public static final String SUFFIX = "$InjectAdapter";

    public static void bind(Activity activity) {
        Class clazz = activity.getClass();
        try {
            Class<InjectAdapter<Activity>> viewBindingClass = (Class<InjectAdapter<Activity>>) Class.forName(clazz.getName() + SUFFIX);
            InjectAdapter<Activity> adapter = viewBindingClass.newInstance();
            adapter.bind(activity);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
