package com.platform.sdk.wx.share;

import android.graphics.Bitmap;

import com.platform.app.config.PlatformConfigurator;
import com.platform.app.config.PlatformType;
import com.platform.app.share.ShareParams;
import com.platform.app.share.util.ShareObservable;
import com.platform.app.share.util.SingleImageLoader;
import com.platform.sdk.wx.WXApi;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

/**
 * @Author: luqihua
 * @Time: 2018/5/23
 * @Description: WXShareToll
 */

public class WXShareTool {

    /**
     * app直接打开微信小程序
     *
     * @param path 小程序对应页面
     */
    public void launchMiniProgram(final String path) {
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = PlatformConfigurator.getInstance().getConfiguration(PlatformType.MINI_PROGRAM); // 填小程序原始id
        req.path = path;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        if (PlatformConfigurator.getInstance().isDebug()) {
            req.miniprogramType = 2;// 可选打开 开发版，体验版和正式版

        } else {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        }
        WXApi.getWXAPI().sendReq(req);
    }

    /**
     * 分享小程序卡片
     *
     * @param params
     */
    public void shareMiniProgram(final ShareParams params) {
        //由于小程序分享的大图需要byte[]  因此  先加载图片
        SingleImageLoader.loadBitmap(params.mSimpleImage, new SingleImageLoader.LoadCallback() {
            @Override
            public void onLoadSuccess(Bitmap bitmap) {
                WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
                if (PlatformConfigurator.getInstance().isDebug()) {
                    miniProgramObj.miniprogramType = 2;
                } else {
                    miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
                }
                //小程序的  原始id
                miniProgramObj.userName = PlatformConfigurator.getInstance().getConfiguration(PlatformType.MINI_PROGRAM);     // 小程序原始id
                miniProgramObj.webpageUrl = params.mUrl;  // 兼容低版本的网页链接
                miniProgramObj.path = params.mPagePath;   //小程序页面路径

                WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
                msg.title = params.mTitle;                      // 小程序消息title
                msg.description = params.mText;// 小程序消息desc
                if (bitmap != null) {
                    msg.setThumbImage(bitmap);// 小程序消息封面图片，小于128k
                }

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = generateTransaction();//transaction用于表示唯一的请求
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前支持会话
                WXApi.getWXAPI().sendReq(req);
            }

            @Override
            public void onError(String message) {
                ShareObservable.getInstance().publishFailed(PlatformType.WEIXIN, message);
            }
        });
    }


    /**
     * 分享到会话或者朋友圈
     *
     * @param req_type 表示分享到朋友圈还是好友会话
     * @param params
     */
    public void shareWechat(final int req_type, final ShareParams params) {
        SingleImageLoader.loadBitmap(params.mSimpleImage, new SingleImageLoader.LoadCallback() {
            @Override
            public void onLoadSuccess(Bitmap bitmap) {
                WXWebpageObject webpageObject = new WXWebpageObject();
                webpageObject.webpageUrl = params.mUrl;

                WXMediaMessage msg = new WXMediaMessage(webpageObject);
                msg.title = params.mTitle;
                msg.description = params.mText;
                if (bitmap != null) {
                    msg.setThumbImage(bitmap);// 封面图片，小于128k
                }

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = generateTransaction();
                req.message = msg;
                req.scene = req_type;

                WXApi.getWXAPI().sendReq(req);
            }

            @Override
            public void onError(String message) {
                ShareObservable.getInstance().publishFailed(PlatformType.WEIXIN, message);
            }
        });

    }

    private String generateTransaction() {
        return "" + System.currentTimeMillis() + "";
    }

}
