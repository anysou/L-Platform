package com.lu.platform.sdk.wx.login;

import android.text.TextUtils;
import android.util.Log;

import com.lu.platform.app.config.ConfigEntity;
import com.lu.platform.app.config.Constants;
import com.lu.platform.app.config.PlatformConfigurator;
import com.lu.platform.app.login.LoginResult;
import com.lu.platform.app.util.PHttpCallback;
import com.lu.platform.app.util.PHttpUtil;
import com.lu.platform.sdk.wx.WXApi;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * author: luqihua
 * date:2018/8/7
 * description:
 **/
public class WXLoginTool {
    public void login(final String refreshToken){
        if (!TextUtils.isEmpty(refreshToken)){
           refreshAccess(refreshToken);
        }else {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";//请求获取用户信息
            req.state = "session";
            WXApi.getWXAPI().sendReq(req);
        }
    }


    public static void getAccessToken(final String code){
        //   appid	是	应用唯一标识，在微信开放平台提交应用审核通过后获得
        //   secret	是	应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
        //   code	是	填写第一步获取的code参数
        //   grant_type	是	填authorization_code
        ConfigEntity entity = PlatformConfigurator.getInstance().getConfiguration(Constants.WEIXIN);
        final String appId = entity.getAppid();
        final String appSecret = entity.getArg2();
        final String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                + "appid=" + appId
                + "&secret=" + appSecret
                + "&code=" + code
                + "&grant_type=authorization_code";
        new PHttpUtil().get(url, new PHttpCallback() {
            @Override
            public void onSuccess(String data) {
                Log.d("WXLoginTool", data);
                LoginResult.success(Constants.WEIXIN, (data));
            }

            @Override
            public void onError(String errorMessage) {
               LoginResult.error(Constants.WEIXIN,errorMessage);
            }
        });
    }

    public  void refreshAccess(final String refresh_token) {
        ConfigEntity entity = PlatformConfigurator.getInstance().getConfiguration(Constants.WEIXIN);
        final String appId = entity.getAppid();
        final String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?"+
                "appid="+appId
                +"&grant_type=refresh_token"+
                "&refresh_token="+refresh_token;
        new PHttpUtil().get(url, new PHttpCallback() {
            @Override
            public void onSuccess(String data) {
                Log.d("WXLoginTool", data);
                try {
                    JSONObject object = new JSONObject(data);
                    if (object.isNull("errcode")) {
                        LoginResult.success(Constants.WEIXIN,object);
                    }else {
                        final SendAuth.Req req = new SendAuth.Req();
                        req.scope = "snsapi_userinfo";//请求获取用户信息
                        req.state = "session";
                        WXApi.getWXAPI().sendReq(req);
                    }
                } catch (JSONException e) {
                    LoginResult.error(Constants.WEIXIN,e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                LoginResult.error(Constants.WEIXIN, errorMessage);
            }
        });
    }
}
