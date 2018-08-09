package com.lu.platform.app.share;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.lu.platform.sdk.qq.share.QQShareTool;
import com.lu.platform.sdk.sina.share.SinaShareTool;
import com.lu.platform.sdk.wx.share.WXShareTool;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

/**
 * @Author: luqihua
 * @Time: 2018/5/25
 * @Description: 执行分享操作的代理类
 */

public class ShareDelegate implements IShareAction {

    private Activity mActivity;
    private ShareParams mParams;

    private QQShareTool mQQShareTool;
    private SinaShareTool mSinaShareTool;
    private WXShareTool mWXShareTool;

    public ShareDelegate(Activity activity, ShareParams params) {
        this.mActivity = activity;
        this.mParams = params;
    }

    public void onNewIntent(Intent intent) {
        if (mSinaShareTool != null)
            mSinaShareTool.onIntentResult(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mQQShareTool != null)
            mQQShareTool.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void shareWXCircle() {
        mWXShareTool = new WXShareTool();
        mWXShareTool.shareWechat(SendMessageToWX.Req.WXSceneTimeline, mParams);
    }

    @Override
    public void shareWechat() {
        mWXShareTool = new WXShareTool();
        mWXShareTool.shareWechat(SendMessageToWX.Req.WXSceneSession, mParams);
    }

    @Override
    public void shareQQ() {
        mQQShareTool = new QQShareTool();
        mQQShareTool.share2QQ(mActivity, mParams);
    }

    @Override
    public void shareQZone() {
        mQQShareTool = new QQShareTool();
        mQQShareTool.share2Qzone(mActivity, mParams);
    }

    @Override
    public void shareSinaWB() {
        mSinaShareTool = new SinaShareTool(mActivity);
        mSinaShareTool.share(mParams);
    }

    @Override
    public void copyLink() {
        //复制数据到剪切板
        ClipboardManager manager = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("data", mParams.mUrl);//这个值会插入到第一个item
        manager.setPrimaryClip(clipData);
        Toast.makeText(mActivity, "链接已复制到剪切板", Toast.LENGTH_SHORT).show();
        mActivity.finish();
    }

}
