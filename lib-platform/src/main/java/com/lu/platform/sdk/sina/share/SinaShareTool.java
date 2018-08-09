package com.lu.platform.sdk.sina.share;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.lu.platform.app.config.Constants;
import com.lu.platform.app.share.ShareParams;
import com.lu.platform.app.share.ShareObservable;
import com.lu.platform.app.util.SingleImageLoader;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

/**
 * @Author: luqihua
 * @Time: 2018/5/23
 * @Description: SinaShareTool
 */

public class SinaShareTool {

    private WbShareHandler wbShareHandler;

    public SinaShareTool(Activity activity) {
        try {
            //令WBApi中的static方法执行
            Class.forName("com.platform.sdk.sina.SinaApi");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        wbShareHandler = new WbShareHandler(activity);
        wbShareHandler.registerApp();
    }

    public void share(final ShareParams mParams) {
        SingleImageLoader.loadBitmap(mParams.mSimpleImage, new SingleImageLoader.LoadCallback() {
            @Override
            public void onLoadSuccess(Bitmap bitmap) {
                //添加文字信息
                TextObject textObject = new TextObject();
                textObject.title = mParams.mTitle;
                textObject.text = mParams.mText;
                textObject.actionUrl = mParams.mUrl;


                //添加图片
                ImageObject imageObject = new ImageObject();
                if (bitmap!=null){
                    imageObject.setImageObject(bitmap);
                }

                WeiboMultiMessage multiMessage = new WeiboMultiMessage();
                multiMessage.textObject = textObject;
                multiMessage.imageObject = imageObject;


                wbShareHandler.shareMessage(multiMessage, false);
            }

            @Override
            public void onError(String message) {
                ShareObservable.getInstance().publishFailed(Constants.SINA,message);
            }
        });


    }

    /**
     * 结果回调
     *
     * @param intent
     */
    public void onIntentResult(Intent intent) {
        wbShareHandler.doResultIntent(intent, wbShareCallback);
    }


    private WbShareCallback wbShareCallback = new WbShareCallback() {
        @Override
        public void onWbShareSuccess() {
            ShareObservable.getInstance().publishSuccess();
        }

        @Override
        public void onWbShareCancel() {
            Log.d("SinaShareTool", "onWbShareCancel");
        }

        @Override
        public void onWbShareFail() {
            ShareObservable.getInstance().publishFailed(Constants.SINA, "分享失败");
        }
    };
}
