package com.lu.platform.sdk.wx.pay;

import android.content.Context;

import com.lu.platform.app.config.Constants;
import com.lu.platform.app.pay.PayObservable;
import com.lu.platform.sdk.wx.WXApi;
import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * @Author: luqihua
 * @Time: 2018/4/20
 * @Description: WXPayUtil
 */

public class WXPayUtil {
    private Builder mBuilder;

    private WXPayUtil(Builder builder) {
        this.mBuilder = builder;
    }

    public void pay() {
        if (!WXApi.getWXAPI().isWXAppInstalled() || !WXApi.getWXAPI().isWXAppSupportAPI()) {
            PayObservable.getInstance().publishFailed(Constants.WEIXIN, "未安装微信客户端");
            return;
        }

        PayReq payReq = new PayReq();
        payReq.appId = mBuilder.appid;
        payReq.partnerId = mBuilder.partnerid;
        payReq.prepayId = mBuilder.prepayid;
        payReq.nonceStr = mBuilder.noncestr;
        payReq.timeStamp = mBuilder.timestamp;
        payReq.sign = mBuilder.sign;
        payReq.packageValue = mBuilder.packageValue;
        /*回调时在WXPayEntryActivity中的onResp方法*/
        WXApi.getWXAPI().sendReq(payReq);
    }

    public static class Builder {
        private Context context;
        private String appid;//	是		微信开放平台审核通过的应用APPID
        private String partnerid;//	是		微信支付分配的商户号
        private String prepayid;//	是		微信返回的支付交易会话ID
        private String noncestr;//		是		随机字符串，不长于32位。推荐随机数生成算法
        private String timestamp;//	是		时间戳，请见接口规则-参数规定
        private String sign;//		是		签名，详见签名生成算法注意：签名方式一定要与统一下单接口使用的一致
        private String packageValue;//  "Sign=WXPay"
        private String app_secret;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setAppid(String appid) {
            this.appid = appid;
            return this;
        }

        public Builder setPartnerid(String partnerid) {
            this.partnerid = partnerid;
            return this;
        }

        public Builder setPrepayid(String prepayid) {
            this.prepayid = prepayid;
            return this;
        }

        public Builder setNoncestr(String noncestr) {
            this.noncestr = noncestr;
            return this;
        }

        public Builder setTimestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setSign(String sign) {
            this.sign = sign;
            return this;
        }

        public Builder setPackageValue(String packageValue) {
            this.packageValue = packageValue;
            return this;
        }

        public Builder setApp_secret(String app_secret) {
            this.app_secret = app_secret;
            return this;
        }

        public WXPayUtil build() {
            return new WXPayUtil(this);
        }
    }
}
