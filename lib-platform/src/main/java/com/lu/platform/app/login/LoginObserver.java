package com.lu.platform.app.login;

import com.lu.platform.app.config.Constants;

/**
 * author: luqihua
 * date:2018/8/8
 * description:
 **/
public interface LoginObserver {
    void loginSuccess(@Constants.PlatformType String platformType, LoginObj obj);

    void loginError(@Constants.PlatformType String platformType, String errMessage);
}
