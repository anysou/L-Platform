package com.lu.platform.sdk.qq.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.lu.platform.app.config.ConfigEntity;
import com.lu.platform.app.config.Constants;
import com.lu.platform.app.config.PlatformConfigurator;
import com.lu.platform.app.login.LoginResult;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * author: luqihua
 * date:2018/8/7
 * description:
 **/
public class QQLoginTool {
    private Tencent mTencent;

    public QQLoginTool() {
        //创建Tencent实例
        Context context = PlatformConfigurator.getInstance().getContext();
        ConfigEntity entity = PlatformConfigurator.getInstance().getConfiguration(Constants.QQ);
        mTencent = Tencent.createInstance(entity.getAppid(), context);
    }

    public void login(Activity activity, final String openId, final String accessToken, final String expiresIn) {
        mTencent.setOpenId(openId);
        mTencent.setAccessToken(accessToken, expiresIn);
        if (!mTencent.isSessionValid())
            mTencent.login(activity, "get_user_info", mIUiListener);
        else {
            mTencent.logout(activity);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
    }

    private IUiListener mIUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            LoginResult.success(Constants.QQ, (JSONObject) o);
        }

        @Override
        public void onError(UiError uiError) {
            LoginResult.error(Constants.QQ,uiError.errorMessage);
        }

        @Override
        public void onCancel() {
           LoginResult.error(Constants.QQ, "用户取消操作");
        }
    };
}
