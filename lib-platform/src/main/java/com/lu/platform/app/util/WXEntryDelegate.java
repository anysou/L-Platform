package com.lu.platform.app.util;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lu.platform.app.config.Constants;
import com.lu.platform.app.share.ShareObservable;
import com.lu.platform.sdk.wx.WXApi;
import com.lu.platform.sdk.wx.login.WXLoginTool;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * 微信分享，登录等回调
 */
public class WXEntryDelegate extends Activity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXApi.getWXAPI().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WXApi.getWXAPI().handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        //分享的回调
        if (resp instanceof SendMessageToWX.Resp) {
            finish();
            if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
                //分享成功
                if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                    ShareObservable.getInstance().publishSuccess();
                } else {
                    ShareObservable.getInstance().publishFailed(Constants.WEIXIN, "分享失败");
                }
            }
            //登录回调
        } else if (resp instanceof SendAuth.Resp) {
            finish();
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                final String code = ((SendAuth.Resp) resp).code;
                WXLoginTool.getAccessToken(code);
            } else if (resp.errCode == BaseResp.ErrCode.ERR_AUTH_DENIED) {
                //用户拒绝授权登录
            } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                //用户取消操作
            }
        }
    }
}