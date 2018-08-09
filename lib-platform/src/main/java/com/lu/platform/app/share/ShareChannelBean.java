package com.lu.platform.app.share;

import android.support.annotation.IdRes;

/**
 * author: luqihua
 * date:2018/7/30
 * description:
 **/
public class ShareChannelBean {
    private String mChannelName;
    private @IdRes int mChannelIcon;

    public ShareChannelBean() {
    }

    public ShareChannelBean(String channelName, int channelIcon) {
        this.mChannelName = channelName;
        this.mChannelIcon = channelIcon;
    }

    public String getChannelName() {
        return mChannelName;
    }

    public void setChannelName(String mChannelName) {
        this.mChannelName = mChannelName;
    }

    public int getChannelIcon() {
        return mChannelIcon;
    }

    public void setChannelIcon(int mChannelIcon) {
        this.mChannelIcon = mChannelIcon;
    }
}
