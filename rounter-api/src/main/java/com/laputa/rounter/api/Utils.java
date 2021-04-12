package com.laputa.rounter.api;

import android.app.Activity;

/**
 * Author by xpl, Date on 2021/4/12.
 */
public class Utils {

    /**
     * 寻找生成的FieldGet规则的完整类名
     * @param clz
     * @param prefix
     * @return
     */
    public static String findActivityFieldClassName(Class<? extends Activity> clz, String prefix) {
        return BuildConfig.packageNameForAPT + "." + clz.getSimpleName() + prefix;
    }
}
