package com.lu.platform.app.pay;

import com.lu.platform.app.config.Constants;

/**
 * @Author: luqihua
 * @Time: 2018/4/23
 * @Description: PayOberver
 */

public interface PayObserver {
    void onPaySuccess(@Constants.PlatformType String payType, String resultMessage);

    void onPayFailed(@Constants.PlatformType String payType, String errorMessage);
}
