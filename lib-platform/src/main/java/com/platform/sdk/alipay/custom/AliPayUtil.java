package com.platform.sdk.alipay.custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.platform.sdk.alipay.sdk.pay.PayResult;
import com.platform.sdk.alipay.sdk.pay.util.OrderInfoUtil2_0;
import com.platform.app.config.PlatformType;
import com.google.gson.Gson;
import com.platform.app.utils.PayObservable;

import java.util.Map;

/**
 * @Author: luqihua
 * @Time: 2018/4/19
 * @Description: AliPayUtil
 */

public class AliPayUtil {
//    /**
//     * 支付宝支付业务：入参app_id
//     */
//    public static final String APPID = "2017052607357406";
//
//    /** 商户私钥，pkcs8格式 */
//    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
//    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
//    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
//    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
//    /**
//     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
//     */
//    public static final String RSA2_PRIVATE = "";
//    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJrnjrjNpoJF/Wsl6lAF74AQgBR3szztO5e1ClQkA1D64Op8K3FHD+xd3YZx7LOA+TLu922yMrm3T9P1gfVY271yIv/YOhGksv/5W7c6zdPSb+XafEokd87nrXMwxbOBx8lwGcrA7qmb++moFfiu3ssRTuT2MD3xO72hxgZ5OKebAgMBAAECgYAFf25rVLEyMnW6FdBpIqzLaMqC73SnFuxkiAJj1htgBmdxKxMHpDQGeZZhLS3veQZfLmgNtqNiQAVPSolgQdUaqAznqslrGAJSVnmwmVt92kHYtRnAUg+Pq+L2FSfKwZ/z1yTgZxRDSxA1rg6Kz+yQ17lSCUgWhUNLL6qa+E0vkQJBAMtTp2v3uI/ZIQc9DeuaHuprzci1m29AY4kHDVrBiISRPpT7uWQa20eJ3r+g8430/Nxwiby+xjaHO3vR8Nfkx/UCQQDDCJuX8ZOyTS0ANFXEvH0F/Eicnx/e1Q4Zdmo5vp6d0qqnFxLoh9LOcmarPTvjIMBOms5H9R8XnclckPXVQkdPAkBpgXwTy1Rq2FmKHwVVJwE51TzQFGSWgwvjM9SbLme/mnql5kUaC9GTOphqSYN4OxLm9EKUvU2vR9b29kaKUL3tAkEApRuPZRU9HO/azdaAWXncOYn3CqhnOaSSOBoYGWR3MVKb57JP7oy1eNI+em7vWPtr8d96DE7GEJ8iTXvmsbBXXwJAbTUpoekpJsRaJQu1AT+mVMcY2KNdd9CZ9qSU5gjzacGS3x47GiLeU4M0xd4gJBnC4q0nETXJKk24RWRAZzvgtQ==";

    private static final int SDK_PAY_FLAG = 1;

    private Activity mActivity;
    private String mAppId;
    private BizContent mBizContent;//业务参数包装类
    private String RSA_PRIVATE;
    private String RSA2_PRIVATE;
    private String notify_url;//异步回调地址
    private String orderInfo;//异步回调地址

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.myLooper()) {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
//                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        PayObservable.getInstance().publishSuccess(PlatformType.ALI, "支付成功");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        PayObservable.getInstance().publishFailed(PlatformType.ALI, "支付失败");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private AliPayUtil(@NonNull Builder builder) {
        this.mActivity = builder.activity;
        this.mAppId = builder.app_id;
        this.mBizContent = builder.biz_content;
        this.RSA_PRIVATE = builder.RSA_PRIVATE;
        this.RSA2_PRIVATE = builder.RSA2_PRIVATE;
        this.notify_url = builder.notify_url;
        this.orderInfo = builder.orderInfo;
    }

    /**
     * @alipay 支付宝支付业务
     */
    public void pay() {

        //如果有已经加密过的orderInfo，则直接发起支付
        if (!TextUtils.isEmpty(orderInfo)) {
            payWidthOrderInfo(orderInfo, true);
            return;
        }

        //如果orderInfo为空  说明要进行本地参数拼接加密
        if (TextUtils.isEmpty(mAppId)
                || (TextUtils.isEmpty(RSA_PRIVATE) && TextUtils.isEmpty(RSA2_PRIVATE))) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("警告")
                    .setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }
        //判断是否使用rsa2加签
        boolean rsa2 = !TextUtils.isEmpty(RSA2_PRIVATE);
        //构造支付参数集合
        final String biz_content = new Gson().toJson(mBizContent);//业务参数包装类需要转成json
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(mAppId, rsa2, biz_content, notify_url);
        //将支付参数集合拼接成字符串
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        //对支付参数进行加签
        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        //加验签拼接到参数字符串后面
        final String orderInfo = orderParam + "&" + sign;

        //异步发起支付请求
        payWidthOrderInfo(orderInfo, true);
    }


    private void payWidthOrderInfo(final String orderInfo, final boolean showDialog) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(orderInfo, showDialog);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }).start();
    }


    public static class Builder {
        Activity activity;
        String app_id;
        String RSA_PRIVATE;
        String RSA2_PRIVATE;
        BizContent biz_content;
        String notify_url;
        String orderInfo;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setAPPID(String app_id) {
            this.app_id = app_id;
            return this;
        }

        public Builder setRSA_PRIVATE(String RSA_PRIVATE) {
            this.RSA_PRIVATE = RSA_PRIVATE;
            return this;
        }

        public Builder setRSA2_PRIVATE(String RSA2_PRIVATE) {
            this.RSA2_PRIVATE = RSA2_PRIVATE;
            return this;
        }

        public Builder setBiz_content(BizContent biz_content) {
            this.biz_content = biz_content;
            return this;
        }

        public Builder setNotify_url(String notify_url) {
            this.notify_url = notify_url;
            return this;
        }

        public Builder setOrderInfo(String orderInfo) {
            this.orderInfo = orderInfo;
            return this;
        }

        public AliPayUtil build() {
            return new AliPayUtil(this);
        }
    }
}
