package com.platform.sdk.wx;

import android.content.Context;

import com.platform.app.config.ConfigEntity;
import com.platform.app.config.PlatformConfigurator;
import com.platform.app.config.PlatformType;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;

/**
 * @Author: luqihua
 * @Time: 2018/5/22
 * @Description: WXApi
 */

public class WXApi {

    private static WeakReference<IWXAPI> sIwxapiWeakReference;

    static {
        initialize();
    }

    public static IWXAPI getWXAPI() {
        IWXAPI iwxapi = sIwxapiWeakReference.get();
        if (iwxapi == null) {
            initialize();
            iwxapi = sIwxapiWeakReference.get();
        }
        return iwxapi;
    }


    private static void initialize() {
        Context context = PlatformConfigurator.getInstance().getContext();
        ConfigEntity entity = PlatformConfigurator.getInstance().getConfiguration(PlatformType.WEIXIN);
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, entity.getAppid(), true);
        iwxapi.registerApp(entity.getAppid());
        sIwxapiWeakReference = new WeakReference<>(iwxapi);
    }

}
