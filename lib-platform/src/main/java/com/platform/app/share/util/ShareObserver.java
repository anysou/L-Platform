package com.platform.app.share.util;

import com.platform.app.config.PlatformType;

/**
 * @Author: luqihua
 * @Time: 2018/5/25
 * @Description: ShareObserver
 */

public interface ShareObserver {
    void onShareSuccess();

    void onShareFailed(Enum<PlatformType> typeEnum, String message);
}
