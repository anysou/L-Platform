package com.lu.platform.app.login;

import com.lu.platform.app.config.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * author: luqihua
 * date:2018/8/8
 * description:
 **/
public class LoginResult {

    public static void success(final String platformType, String data) {
        try {
            success(platformType,new JSONObject(data));
        } catch (JSONException e) {
            error(platformType, "结果解析出错");
        }
    }

    public static void success(final String platformType, JSONObject jsonObject) {
        try {
            LoginObj loginObj = new LoginObj();
            loginObj.setAccess_token(jsonObject.getString("access_token"));
            loginObj.setOpenid(jsonObject.getString("openid"));
            loginObj.setExpires_in(jsonObject.getString("expires_in"));

            if (platformType.equalsIgnoreCase(Constants.WEIXIN)) {
                loginObj.setExpires_in(jsonObject.getString("expires_in"));
            }
            LoginObservable.getInstance().publishSuccess(platformType, loginObj);
        } catch (JSONException e) {
            error(platformType, "结果解析出错");
        }
    }

    public static void error(final String platformType, String message) {
        LoginObservable.getInstance().publishFailed(platformType, message);
    }
}
