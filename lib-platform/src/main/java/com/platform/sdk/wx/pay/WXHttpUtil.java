package com.platform.sdk.wx.pay;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: luqihua
 * @Time: 2018/4/20
 * @Description: 用于发送网络请求   主要用于统一下单请求
 */

public class WXHttpUtil {
    //微信统一下单的接口
    private static final String WX_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private static final int HANDLER_CODE_SUCCESS = 0x12;
    private static final int HANDLER_CODE_ERROR = 0x14;

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            WXHttpCallback callback = mCallbackWeakReference.get();
            if (callback == null) return;
            switch (msg.what) {
                case HANDLER_CODE_SUCCESS:
                    callback.onSuccess((String) msg.obj);
                    break;
                case HANDLER_CODE_ERROR:
                    callback.onError((String) msg.obj);
                    break;
            }
        }
    };


    private String mOrderInfo;
    private WeakReference<WXHttpCallback> mCallbackWeakReference;

    public WXHttpUtil(String orderInfo, WXHttpCallback callback) {
        this.mOrderInfo = orderInfo;
        this.mCallbackWeakReference = new WeakReference<WXHttpCallback>(callback);
    }

    public void prePay() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                ByteArrayOutputStream bos = null;
                try {
                    conn = (HttpURLConnection) new URL(WX_UNIFIED_ORDER).openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(10000);
                    conn.connect();

                    OutputStream writer = conn.getOutputStream();
                    writer.write(mOrderInfo.getBytes());
                    writer.flush();
                    writer.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode != 200) {
                        mHandler.obtainMessage(HANDLER_CODE_ERROR, responseCode + " 响应错误").sendToTarget();
                        return;
                    }

                    InputStream in = conn.getInputStream();

                    bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        bos.write(buffer, 0, len);
                    }
                    in.close();
                    //成功获取到服务器返回的数据
                    mHandler.obtainMessage(HANDLER_CODE_SUCCESS, bos.toString()).sendToTarget();
                } catch (IOException e) {
                    mHandler.obtainMessage(HANDLER_CODE_ERROR, e.getMessage()).sendToTarget();
                } finally {
                    try {
                        if (bos != null) {
                            bos.close();
                        }
                        if (conn != null) {
                            conn.disconnect();
                        }
                    } catch (IOException e) {
                        mHandler.obtainMessage(HANDLER_CODE_ERROR, e.getMessage()).sendToTarget();
                    }
                }
            }
        }).start();
    }

}
