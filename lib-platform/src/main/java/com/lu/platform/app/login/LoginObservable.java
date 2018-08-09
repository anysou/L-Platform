package com.lu.platform.app.login;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * author: luqihua
 * date:2018/8/8
 * description:
 **/
public class LoginObservable {
    private List<WeakReference<LoginObserver>> mLoginObservers = new ArrayList<>();

    private LoginObservable() {
    }

    private static class Holder {
        private static LoginObservable sInstance = new LoginObservable();
    }

    public static LoginObservable getInstance() {
        return LoginObservable.Holder.sInstance;
    }

    /**
     * 发布分享成功的通知
     */
    public void publishSuccess(final String platformType,LoginObj loginObj) {
        for (WeakReference<LoginObserver> reference : mLoginObservers) {
            if (reference.get() == null) continue;
            reference.get().loginSuccess(platformType,loginObj);
        }
    }

    /**
     * 发布分享失败的通知
     *
     * @param errorMessage
     */
    public void publishFailed(String type, String errorMessage) {
        for (WeakReference<LoginObserver> reference : mLoginObservers) {
            if (reference.get() == null) continue;
            reference.get().loginError(type, errorMessage);
        }
    }

    /**
     * 注册订阅者
     *
     * @param observer
     */
    public void register(LoginObserver observer) {
        if (observer == null) return;
        for (WeakReference<LoginObserver> reference : mLoginObservers) {
            //防止多次注册
            if (reference.get().equals(observer)) return;
        }
        mLoginObservers.add(new WeakReference<>(observer));
    }

    /**
     * 取消订阅
     *
     * @param observer
     */
    public void unRegister(LoginObserver observer) {
        if (observer == null) return;
        int count = mLoginObservers.size();
        if (count == 0) return;
        for (int i = 0; i < count; i++) {
            WeakReference<LoginObserver> reference = mLoginObservers.get(i);
            if (reference.get() == null) continue;
            if (reference.get().equals(observer)) {
                reference.clear();
                mLoginObservers.remove(reference);
                break;
            }
        }
    }
}
