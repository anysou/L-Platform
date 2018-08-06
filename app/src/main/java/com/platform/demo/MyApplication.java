package com.platform.demo;

import android.app.Application;

import com.platform.app.config.PlatformConfigurator;

/**
 * @Author: luqihua
 * @Time: 2018/6/1
 * @Description: MyApplication
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //下面的初始化配置为各自平台的信息
        PlatformConfigurator.getInstance()
                .isDebug(BuildConfig.DEBUG)
                .withContext(this)
                .setSINAConfig("", "", "")
                .setQQConfig("", "")
                .setMiniProgramConfig("")
                .setWXConfig("", "")
                .initialize();
    }
}
