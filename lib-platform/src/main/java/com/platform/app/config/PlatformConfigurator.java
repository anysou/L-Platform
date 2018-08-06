package com.platform.app.config;

import android.content.Context;

import java.util.HashMap;

/**
 * @Author: luqihua
 * @Time: 2018/5/23
 * @Description: PlatformConfigurator
 */

public class PlatformConfigurator {

    private HashMap<String, Object> configurationMap = new HashMap<>();
    private final String IS_READY = "isready";
    private final String CONTEXT = "context";
    private final String IS_DEBUG = "is_debug";

    private PlatformConfigurator() {
        configurationMap.put(IS_READY, false);
        configurationMap.put(IS_DEBUG, false);
    }

    private static class Holder {
        private static PlatformConfigurator sInstance = new PlatformConfigurator();
    }

    public static PlatformConfigurator getInstance() {
        return Holder.sInstance;
    }

    public PlatformConfigurator withContext(Context context) {
        configurationMap.put(CONTEXT, context.getApplicationContext());
        return this;
    }

    public PlatformConfigurator isDebug(boolean isdebug) {
        configurationMap.put(IS_DEBUG, isdebug);
        return this;
    }

    public PlatformConfigurator setQQConfig(String appid, String appSecret) {
        ConfigEntity entity = new ConfigEntity(appid, appSecret);
        configurationMap.put(PlatformType.QQ.name(), entity);
        return this;
    }

    public PlatformConfigurator setWXConfig(String appid, String appSecret) {
        ConfigEntity entity = new ConfigEntity(appid, appSecret);
        configurationMap.put(PlatformType.WEIXIN.name(), entity);
        return this;
    }

    public PlatformConfigurator setMiniProgramConfig(String programId) {
        configurationMap.put(PlatformType.MINI_PROGRAM.name(), programId);
        return this;
    }

    public PlatformConfigurator setALIConfig(String appid) {
        configurationMap.put(PlatformType.ALI.name(), appid);
        return this;
    }


    public PlatformConfigurator setSINAConfig(String appid, String appSecret, String redirectUrl) {
        ConfigEntity entity = new ConfigEntity(appid, appSecret, redirectUrl);
        configurationMap.put(PlatformType.SINA.name(), entity);
        return this;
    }

    /**
     * 调用这个方法表示初始化完成
     */
    public void initialize() {
        configurationMap.put(IS_READY, true);
    }


    /**
     * 获取上下文
     *
     * @return
     */
    public Context getContext() {
        checkInitialize();
        return (Context) configurationMap.get(CONTEXT);
    }

    /**
     * 是否是debug模式
     *
     * @return
     */
    public boolean isDebug() {
        checkInitialize();
        return (boolean) configurationMap.get(IS_READY);
    }

    /**
     * 取出配置项
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getConfiguration(Enum<PlatformType> key) {
        checkInitialize();
        return (T) configurationMap.get(key.name());
    }

    private void checkInitialize() {
        boolean isReady = (boolean) configurationMap.get(IS_READY);
        Context context = (Context) configurationMap.get(CONTEXT);
        if (!isReady) {
            throw new RuntimeException("PlatformConfigurator: please call method initialize()");
        }
        if (context == null) {
            throw new RuntimeException("PlatformConfigurator: please call method withContext(Context context)");
        }
    }
}
