package com.lu.platform.app.config;

import android.content.Context;

import java.util.HashMap;

/**
 * @Author: luqihua
 * @Time: 2018/5/23
 * @Description: 用于配置第三方平台的一些参数
 */

public class PlatformConfigurator {

    private HashMap<String, Object> configurationMap = new HashMap<>();


    private PlatformConfigurator() {
        configurationMap.put(Constants.IS_READY, false);
        configurationMap.put(Constants.IS_DEBUG, false);
    }

    private static class Holder {
        private static PlatformConfigurator sInstance = new PlatformConfigurator();
    }

    public static PlatformConfigurator getInstance() {
        return Holder.sInstance;
    }

    public PlatformConfigurator withContext(Context context) {
        configurationMap.put(Constants.CONTEXT, context.getApplicationContext());
        return this;
    }

    public PlatformConfigurator isDebug(boolean isdebug) {
        configurationMap.put(Constants.IS_DEBUG, isdebug);
        return this;
    }

    public PlatformConfigurator setQQConfig(String appid, String appSecret) {
        ConfigEntity entity = new ConfigEntity(appid, appSecret);
        configurationMap.put(Constants.QQ, entity);
        return this;
    }

    public PlatformConfigurator setWXConfig(String appid, String appSecret) {
        ConfigEntity entity = new ConfigEntity(appid, appSecret);
        configurationMap.put(Constants.WEIXIN, entity);
        return this;
    }

    public PlatformConfigurator setMiniProgramConfig(String programId) {
        configurationMap.put(Constants.MINI_PROGRAM, programId);
        return this;
    }

    public PlatformConfigurator setALIConfig(String appid) {
        configurationMap.put(Constants.ALI, appid);
        return this;
    }


    public PlatformConfigurator setSINAConfig(String appid, String appSecret, String redirectUrl) {
        ConfigEntity entity = new ConfigEntity(appid, appSecret, redirectUrl);
        configurationMap.put(Constants.SINA, entity);
        return this;
    }

    /**
     * 调用这个方法表示初始化完成
     */
    public void initialize() {
        configurationMap.put(Constants.IS_READY, true);
    }


    /**
     * 获取上下文
     *
     * @return
     */
    public Context getContext() {
        checkInitialize();
        return (Context) configurationMap.get(Constants.CONTEXT);
    }

    /**
     * 是否是debug模式
     *
     * @return
     */
    public boolean isDebug() {
        checkInitialize();
        return (boolean) configurationMap.get(Constants.IS_READY);
    }

    /**
     * 取出配置项
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getConfiguration(@Constants.PlatformType String key) {
        checkInitialize();
        return (T) configurationMap.get(key);
    }


    public boolean hasConfig(String key) {
        return configurationMap.containsKey(key);
    }


    private void checkInitialize() {
        boolean isReady = (boolean) configurationMap.get(Constants.IS_READY);
        Context context = (Context) configurationMap.get(Constants.CONTEXT);
        if (!isReady) {
            throw new RuntimeException("PlatformConfigurator: please call method initialize()");
        }
        if (context == null) {
            throw new RuntimeException("PlatformConfigurator: please call method withContext(Context context)");
        }
    }
}
