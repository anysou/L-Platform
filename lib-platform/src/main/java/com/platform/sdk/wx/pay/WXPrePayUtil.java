package com.platform.sdk.wx.pay;

import android.content.Context;

import com.platform.app.config.PlatformType;
import com.platform.app.utils.PayObservable;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * 用于请求生成预支付订单，建议预支付订单的生成放在服务端做
 *
 * @Author: luqihua
 * @Time: 2018/4/20
 * @Description: WXPrePayUtil
 */

public class WXPrePayUtil {
    private Builder builder;
    //统一下单成功返回数据后发起支付请求
    private WXHttpCallback callback = new WXHttpCallback() {
        @Override
        public void onSuccess(String data) {
            /*===================解析预支付请求返回的参数=======================*/
            Map<String, String> returnMap = WXUtil.getTAGContent(data);
            final String resultCode = returnMap.get("result_code");

            /*=====================获取到支付所需要的信息并生成sign=====================*/
            TreeMap<String, String> params = new TreeMap<>();
            params.put("appid", returnMap.get("appid"));
            params.put("partnerid", returnMap.get("mch_id"));
            params.put("prepayid", returnMap.get("prepay_id"));
            params.put("noncestr", returnMap.get("nonce_str"));
            params.put("package", "Sign=WXPay");//固定写法
            params.put("timestamp", System.currentTimeMillis() / 1000 + "");//时间戳需要用秒
            String sign = WXUtil.getSign(params, builder.app_secret);

            /*===================发起支付====================*/
            if (resultCode.equals("SUCCESS")) {
                new WXPayUtil.Builder()
                        .setPackageValue(params.get("package"))
                        .setTimestamp(params.get("timestamp"))
                        .setAppid(params.get("appid"))
                        .setPartnerid(params.get("partnerid"))
                        .setNoncestr(params.get("noncestr"))
                        .setPrepayid(params.get("prepayid"))
                        .setSign(sign)
                        .setContext(builder.context)
                        .build()
                        .pay();
            } else {
                PayObservable.getInstance().publishFailed(PlatformType.WEIXIN, "生成预支付订单出错");
            }
        }

        @Override
        public void onError(String errorMessage) {
            PayObservable.getInstance().publishFailed(PlatformType.WEIXIN, "统一下单网络请求失败");
        }
    };


    private WXPrePayUtil(Builder builder) {
        this.builder = builder;
    }

    /**
     * 请求生成预支付订单
     *
     * @throws IOException
     */
    public void startPrePay() {
        /*=================生成sign==================*/
        TreeMap<String, String> params = new TreeMap<>();
        params.put("time_start", WXUtil.getCurrentTime("yyyyMMddHHmmss"));
        params.put("trade_type", "APP");
        params.put("sign_type", "MD5");
        params.put("nonce_str", WXUtil.getRandomStr(20));
        params.put("appid", builder.appid);
        params.put("mch_id", builder.mch_id);
        params.put("body", builder.body);
        params.put("out_trade_no", builder.out_trade_no);
        params.put("total_fee", builder.total_fee);
        params.put("notify_url", builder.notify_url);
        params.put("spbill_create_ip", builder.spbill_create_ip);
        String sign = WXUtil.getSign(params, builder.app_secret);

        /*=====================参数进行xml格式处理=======================*/
        String orderInfo = WXUtil.parseMap2Xml(params, sign);

        try {
            //发起预支付请求
            new WXHttpUtil(orderInfo, callback).prePay();
        } catch (IOException e) {
            PayObservable.getInstance().publishFailed(PlatformType.WEIXIN, "统一下单网络请求失败");
        }
    }


    public static class Builder {
        private Context context;
        private String appid;//	是		微信开放平台审核通过的应用APPID
        private String app_secret;//	是 私钥
        private String mch_id;//	是	微信支付分配的商户号
        private String body;//是	String(128) 商品描述

        private String out_trade_no;//是	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。详见商户订单号
        private String total_fee;//	是	订单总金额，单位为分，详见支付金额
        private String spbill_create_ip;//	是		123.12.12.123	用户端实际ip
        private String notify_url;//	是		http://www.weixin.qq.com/wxpay/pay.php	接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setAppid(String appid) {
            this.appid = appid;
            return this;
        }

        public Builder setApp_secret(String app_secret) {
            this.app_secret = app_secret;
            return this;
        }

        public Builder setMch_id(String mch_id) {
            this.mch_id = mch_id;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
            return this;
        }

        public Builder setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
            return this;
        }

        public Builder setSpbill_create_ip(String spbill_create_ip) {
            this.spbill_create_ip = spbill_create_ip;
            return this;
        }

        public Builder setNotify_url(String notify_url) {
            this.notify_url = notify_url;
            return this;
        }

        public WXPrePayUtil build() {
            return new WXPrePayUtil(this);
        }
    }

}
