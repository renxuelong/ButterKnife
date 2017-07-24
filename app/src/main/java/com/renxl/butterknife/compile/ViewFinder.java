package com.renxl.butterknife.compile;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by renxl
 * On 2017/7/22 18:39.
 */

@SuppressWarnings("unchecked")
public class ViewFinder {

    /**
     * 返回值使用泛型时，会自动将返回值转成需要的类型
     */
    public static <T extends View> T findRequiredView(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }

    public static <T extends View> T findRequiredView(Fragment fragment, int id) {
        return (T) fragment.getActivity().findViewById(id);
    }

    public static <T extends View> T findRequiredView(View view, int id) {
        return (T) view.findViewById(id);
    }
}
