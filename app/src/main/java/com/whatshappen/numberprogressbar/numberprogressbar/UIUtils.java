package com.whatshappen.numberprogressbar.numberprogressbar;

import android.content.Context;

/**
 * @author ww ;
 * @data on 2017/12/26.
 */
public class UIUtils {

    /**
     * DP转PX
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f);
    }

    //px转dp
    public static int px2dp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (scaledDensity * sp);
    }
}
