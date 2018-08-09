package com.lu.platform.sdk.sina;

import android.content.Context;

import com.lu.platform.app.config.ConfigEntity;
import com.lu.platform.app.config.Constants;
import com.lu.platform.app.config.PlatformConfigurator;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

/**
 * @Author: luqihua
 * @Time: 2018/5/24
 * @Description: SinaApi
 */

public class SinaApi {
    /**
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    static {
        Context context = PlatformConfigurator.getInstance().getContext();
        ConfigEntity entity = PlatformConfigurator.getInstance().getConfiguration(Constants.SINA);

        AuthInfo authInfo = new AuthInfo(context, entity.getAppid(), entity.getArg2(), SCOPE);
        WbSdk.install(context, authInfo);
    }
}
