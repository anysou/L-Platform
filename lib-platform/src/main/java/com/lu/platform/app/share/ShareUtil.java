package com.lu.platform.app.share;

import android.app.Activity;
import android.view.View;

import com.lu.platform.sdk.wx.share.WXShareTool;

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

    /**
     * 分享小程序卡片
     *
     * @param params
     */
    public static void shareMiniProgram(ShareParams params) {
        new WXShareTool().shareMiniProgram(params);
    }

    /**
     * 打开小程序
     *
     * @param pagePath 小程序页面地址：例如"pages/index"
     */
    public static void openMiniProgram(final String pagePath) {
        //path填写要打开的小程序页面
        new WXShareTool().launchMiniProgram(pagePath);
    }
}
