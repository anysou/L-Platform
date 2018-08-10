package com.lu.platform.app.login;

import android.app.Activity;

import com.lu.platform.app.share.ShareBaseActivity;
import com.lu.platform.app.share.ShareParams;

/**
 * author: luqihua
 * date:2018/8/10
 * description:
 **/
public class LoginUtil {
    /**
     *
     * @param activity
     * @param loginObj 信息包装类，首次登陆直接new一个或者null
     */
    public static void doLogin(Activity activity, LoginObj loginObj) {
        if (loginObj == null) {
            loginObj = new LoginObj();
        }
        LoginBaseActivity.launch(activity, loginObj);
    }
}
