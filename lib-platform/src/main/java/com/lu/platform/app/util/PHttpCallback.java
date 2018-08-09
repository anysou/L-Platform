package com.lu.platform.app.util;

/**
 * @Author: luqihua
 * @Time: 2018/4/24
 * @Description: WXHttpCallback
 */

public interface PHttpCallback {
    void onSuccess(String data);

    void onError(String errorMessage);
}
