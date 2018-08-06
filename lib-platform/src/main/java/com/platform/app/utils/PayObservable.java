package com.platform.app.utils;

import com.platform.app.config.PlatformType;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: luqihua
 * @Time: 2018/4/23
 * @Description: PayObservable  支付类事件分发中心  分发支付成功和失败
 */

public class PayObservable {
    private static final String TAG = "PayObservable";
    private List<WeakReference<PayObserver>> mPayObservers = new ArrayList<>();

    private PayObservable() {
    }

    private static class Holder {
        private static PayObservable sInstance = new PayObservable();
    }

    public static PayObservable getInstance() {
        return Holder.sInstance;
    }

    /**
     * 发布支付成功的通知
     *
     * @param payType
     * @param resultMessage
     */
    public void publishSuccess(Enum<PlatformType> payType, String resultMessage) {
        for (WeakReference<PayObserver> reference : mPayObservers) {
            if (reference.get() == null) continue;
            reference.get().onPaySuccess(payType, resultMessage);
        }
    }

    /**
     * 发布支付失败的通知
     *
     * @param errorCode
     */
    public void publishFailed(Enum<PlatformType> payType, String errorCode) {
        for (WeakReference<PayObserver> reference : mPayObservers) {
            if (reference.get() == null) continue;
            reference.get().onPayFailed(payType, errorCode);
        }
    }

    /**
     * 注册支付结果的订阅者
     *
     * @param observer
     */
    public void register(PayObserver observer) {
        if (observer == null) return;
        for (WeakReference<PayObserver> reference : mPayObservers) {
            //防止多次注册
            if (reference.get().equals(observer)) return;
        }
        WeakReference<PayObserver> observerWeakReference = new WeakReference<PayObserver>(observer);
        mPayObservers.add(observerWeakReference);
    }

    /**
     * 取消订阅支付结果
     *
     * @param observer
     */
    public void unRegister(PayObserver observer) {
        if (observer == null) return;
        int count = mPayObservers.size();
        if (count == 0) return;
        for (int i = 0; i < count; i++) {
            WeakReference<PayObserver> reference = mPayObservers.get(i);
            if (reference.get() == null) continue;
            if (reference.get().equals(observer)) {
                mPayObservers.remove(reference);
                break;
            }
        }
    }
}
