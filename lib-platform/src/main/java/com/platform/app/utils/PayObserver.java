package com.platform.app.utils;

import com.platform.app.config.PlatformType;

/**
 * @Author: luqihua
 * @Time: 2018/4/23
 * @Description: PayOberver
 */

public interface PayObserver {
    void onPaySuccess(Enum<PlatformType> payType, String resultMessage);

    void onPayFailed(Enum<PlatformType> payType, String errorMessage);
}
