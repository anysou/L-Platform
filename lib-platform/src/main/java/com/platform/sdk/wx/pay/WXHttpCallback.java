package com.platform.sdk.wx.pay;

/**
 * @Author: luqihua
 * @Time: 2018/4/24
 * @Description: WXHttpCallback
 */

public interface WXHttpCallback {
    void onSuccess(String data);

    void onError(String errorMessage);
}
