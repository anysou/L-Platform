package com.lu.platform.app.util;

import android.icu.text.SymbolTable;

/**
 * author: luqihua
 * date:2018/8/9
 * description:
 **/
public class PlatformObservable {

    private PlatformObservable() {
    }

    private static class Holder{
        private static PlatformObservable sInstance = new PlatformObservable();
    }

    public static PlatformObservable getInstance() {
        return Holder.sInstance;
    }


    public void subscribeLogin() {

    }

    public void subscribeShare() {
    }

    public void subscribePay() {

    }

}
