package com.lu.platform.app.share;

/**
 * @Author: luqihua
 * @Time: 2018/5/25
 * @Description: ShareObserver
 */

public interface ShareObserver {
    void onShareSuccess();

    void onShareFailed(String typeEnum, String message);
}
