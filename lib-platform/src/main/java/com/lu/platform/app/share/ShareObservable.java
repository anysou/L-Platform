package com.lu.platform.app.share;

import android.widget.Toast;

import com.lu.platform.app.config.PlatformConfigurator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: luqihua
 * @Time: 2018/4/23
 * @Description: PayObservable  分享事件分发中心  分发分享成功和失败
 */

public class ShareObservable {
    private static final String TAG = "ShareObservable";
    private List<WeakReference<ShareObserver>> mShareObservers = new ArrayList<>();

    private ShareObservable() {
    }

    private static class Holder {
        private static ShareObservable sInstance = new ShareObservable();
    }

    public static ShareObservable getInstance() {
        return Holder.sInstance;
    }

    /**
     * 发布分享成功的通知
     */
    public void publishSuccess() {
        if (mShareObservers.size() == 0) {
            Toast.makeText(PlatformConfigurator.getInstance().getContext(), "分享成功", Toast.LENGTH_SHORT).show();
        } else {
            for (WeakReference<ShareObserver> reference : mShareObservers) {
                if (reference.get() == null) continue;
                reference.get().onShareSuccess();
            }
        }
    }

    /**
     * 发布分享失败的通知
     *
     * @param errorMessage
     */
    public void publishFailed(String type, String errorMessage) {
        if (mShareObservers.size() == 0) {
            Toast.makeText(PlatformConfigurator.getInstance().getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        } else {
            for (WeakReference<ShareObserver> reference : mShareObservers) {
                if (reference.get() == null) continue;
                reference.get().onShareFailed(type, errorMessage);
            }
        }
    }

    /**
     * 注册订阅者
     *
     * @param observer
     */
    public void register(ShareObserver observer) {
        if (observer == null) return;
        for (WeakReference<ShareObserver> reference : mShareObservers) {
            //防止多次注册
            if (reference.get().equals(observer)) return;
        }
        mShareObservers.add(new WeakReference<>(observer));
    }

    /**
     * 取消订阅
     *
     * @param observer
     */
    public void unRegister(ShareObserver observer) {
        if (observer == null) return;
        int count = mShareObservers.size();
        if (count == 0) return;
        for (int i = 0; i < count; i++) {
            WeakReference<ShareObserver> reference = mShareObservers.get(i);
            if (reference.get() == null) continue;
            if (reference.get().equals(observer)) {
                reference.clear();
                mShareObservers.remove(reference);
                break;
            }
        }
    }
}
