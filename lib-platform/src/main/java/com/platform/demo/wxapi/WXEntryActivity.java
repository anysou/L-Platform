package com.platform.demo.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.platform.app.config.PlatformType;
import com.platform.app.share.util.ShareObservable;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.platform.sdk.wx.WXApi;

/**
 * 微信分享，登录等回调
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

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
        finish();
        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            //分享成功
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                ShareObservable.getInstance().publishSuccess();
            } else {
                ShareObservable.getInstance().publishFailed(PlatformType.WEIXIN, "分享失败");
            }
        }
    }
}