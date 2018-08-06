package com.platform.app.share;

import android.support.annotation.IdRes;

import lu.lib_alipay.R;

/**
 * author: luqihua
 * date:2018/7/30
 * description:
 **/
public enum EChannelBean {
    WX_SEND_PYQ("编辑朋友圈", R.drawable.icon_multi_pic),
    WX_HY("微信好友", R.drawable.icon_wechat),
    WX_PYQ("朋友圈", R.drawable.icon_wxcircle),
    QQ_HY("QQ好友", R.drawable.icon_qq),
    QQ_ZONE("QQ空间", R.drawable.icon_q_zone),
    SINA_WB("微博", R.drawable.icon_sina),
    COPY("复制链接", R.drawable.icon_copy);

    private String mChannelName;
    private @IdRes
    int mChannelIcon;

    EChannelBean(String mChannelName, int mChannelIcon) {
        this.mChannelName = mChannelName;
        this.mChannelIcon = mChannelIcon;
    }

    public String getChannelName() {
        return mChannelName;
    }

    public int getChannelIcon() {
        return mChannelIcon;
    }

}
