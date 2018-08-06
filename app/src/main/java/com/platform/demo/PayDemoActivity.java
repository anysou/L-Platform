package com.platform.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.platform.app.config.PlatformType;
import com.platform.app.utils.PayObservable;
import com.platform.app.utils.PayObserver;
import com.platform.sdk.alipay.custom.AliPayUtil;
import com.platform.sdk.alipay.custom.BizContent;
import com.platform.sdk.wx.pay.WXPayUtil;
import com.platform.sdk.wx.pay.WXPrePayUtil;

public class PayDemoActivity extends AppCompatActivity implements PayObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_demo);
        //注册用于监听支付的回调
        PayObservable.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PayObservable.getInstance().unRegister(this);
    }

    /**
     * 在本地对支付的一些参数进行处理生成orderInfo
     *
     * @param v
     */
    public void localAliPay(View v) {
        //业务数据的包装类   参数含义查看该BizContent类
        BizContent bizContent = new BizContent();
        bizContent.setTotal_amount("");
        bizContent.setProduct_code("");
        bizContent.setTimeout_express("");
        bizContent.setBody("");
        bizContent.setSubject("");
        bizContent.setOut_trade_no("");

        new AliPayUtil.Builder(this)
                .setAPPID("")
                .setRSA_PRIVATE("")
                .setBiz_content(bizContent)
                .setNotify_url("")
                .build()
                .pay();
    }

    /**
     * 支付宝支付真正发起请求时只需要一个经过加密的订单信息字符串orderInfo,这个字符串可以由服务器生成，也可以在本地生成
     *
     * @param v
     */
    public void remoteAliPay(View v) {
        //这个orderInfo应该由服务器生成好返回到客户端，这里只写作伪代码。
        final String orderInfo = "";
        //客户端直接传入orderInfo字符串到支付宝sdk发起请求
        new AliPayUtil.Builder(this)
                .setOrderInfo(orderInfo)
                .build()
                .pay();
    }

    /**
     * 由本地完成预支付订单和支付两个步骤
     *
     * @param v
     */
    public void localWeiXinPay(View v) {
        //参数细节查看WXPrePayUtil.Builder类
        new WXPrePayUtil.Builder()
                .setContext(this)
                .setApp_secret("secret")//微信商户后台可以拿到的私钥
                .setAppid("appid")
                .setBody("body")
                .setMch_id("")
                .setNotify_url("")
                .setOut_trade_no("")
                .setTotal_fee("")
                .setSpbill_create_ip("")
                .build()
                .startPrePay();
    }

    /**
     * 如果由服务器发起预支付生成订单   那么下列的参数应该由服务器返回到客户端，客户端发起支付
     *
     * @param v
     */
    public void remoteWeiXinPay(View v) {
        new WXPayUtil.Builder()
                .setContext(this)
                .setAppid("appid")//appid
                .setNoncestr("noncestr")//随机字符串
                .setPartnerid("partnerid")//商户id
                .setPrepayid("prepayid")//预支付订单号
                .setSign("sign")//服务器对参数按照微信要求的形式加密后返回的验签
                .setTimestamp("timestamp")//时间戳
                .setPackageValue("Sign=WXPay")//固定值"Sign=WXPay"
                .build()
                .pay();
    }

    @Override
    public void onPaySuccess(Enum<PlatformType> payType, String resultMessage) {
        //支付成功
        if (payType == PlatformType.ALI) {
            //支付宝支付成功
        } else if (payType == PlatformType.WEIXIN) {
            //微信支付成功
        }
    }

    @Override
    public void onPayFailed(Enum<PlatformType> payType, String errorMessage) {
        //支付失败
    }
}
