package com.lu.platform.app.config;

import android.support.annotation.StringDef;

/**
 * @Author: luqihua
 * @Time: 2018/5/23
 * @Description: PlatformType
 */
public class Constants {

    public static final String WEIXIN = "weixin";
    public static final String MINI_PROGRAM = "weixin_mini_program";//小程序配置原始id
    public static final String ALI = "ali";
    public static final String QQ = "qq";
    public static final String SINA = "sina";

    @StringDef({WEIXIN, ALI, QQ, SINA, MINI_PROGRAM})
    public @interface PlatformType {
    }


    public static final String IS_READY = "isready";
    public static final String CONTEXT = "context";
    public static final String IS_DEBUG = "is_debug";


    //分享的平台
    public static final String SEND_PYQ = "多图分享";
    public static final String WX_FRIEND = "微信好友";
    public static final String WX_ZONE = "朋友圈";
    public static final String QQ_FRIEND = "QQ好友";
    public static final String QQ_ZONE = "QQ空间";
    public static final String SINA_WEIBO = "新浪微博";
    public static final String COPY_LINK = "复制链接";

}
