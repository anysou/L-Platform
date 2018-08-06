package com.platform.app.share;

import android.app.Activity;

/**
 * @Author: luqihua
 * @Time: 2018/5/25
 * @Description: ShareUtil
 */

public class ShareUtil {
    public static void doShare(Activity activity, ShareParams params) {
        if (params == null) {
            throw new RuntimeException("ShareUtil: params must not be null");
        }
        ShareBaseActivity.launch(activity, params);
    }
}
