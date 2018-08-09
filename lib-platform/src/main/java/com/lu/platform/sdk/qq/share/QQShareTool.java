package com.lu.platform.sdk.qq.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lu.platform.app.config.ConfigEntity;
import com.lu.platform.app.config.Constants;
import com.lu.platform.app.config.PlatformConfigurator;
import com.lu.platform.app.share.ShareParams;
import com.lu.platform.app.share.ShareObservable;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * @Author: luqihua
 * @Time: 2018/5/23
 * @Description: QQShareTool
 */

public class QQShareTool {

    private Tencent mTencent;

    public QQShareTool() {
        //创建Tencent实例
        Context context = PlatformConfigurator.getInstance().getContext();
        ConfigEntity entity = PlatformConfigurator.getInstance().getConfiguration(Constants.QQ);
        mTencent = Tencent.createInstance(entity.getAppid(), context);
    }

    /**
     * 分享到qq好友
     *
     * @param activity
     */
    public void share2QQ(Activity activity, ShareParams params) {

        final Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, params.mTitle);
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, params.mText);
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, params.mUrl);
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, params.mSimpleImage);
//        params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, "音乐链接");
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ(activity, bundle, mIUiListener);
    }

    /**
     * 分享到qq空间
     *
     * @param activity
     */
    public void share2Qzone(Activity activity,ShareParams params) {
        //分享类型
        final Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, params.mTitle);
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, params.mText);
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, params.mUrl);

        ArrayList<String> images = new ArrayList<>();
        images.add(params.mSimpleImage);
        if (params.mMultiImages!=null)
            images.addAll(params.mMultiImages);

        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
        mTencent.shareToQzone(activity, bundle, mIUiListener);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
    }


    private IUiListener mIUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            ShareObservable.getInstance().publishSuccess();
        }

        @Override
        public void onError(UiError uiError) {
            ShareObservable.getInstance().publishFailed(Constants.QQ, uiError.errorMessage);
        }

        @Override
        public void onCancel() {
            Log.d("QQShareTool", "onCancel:");
        }
    };
}
