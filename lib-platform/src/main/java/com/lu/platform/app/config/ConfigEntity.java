package com.lu.platform.app.config;

/**
 * @Author: luqihua
 * @Time: 2018/5/23
 * @Description: ConfigEntity
 */

public class ConfigEntity {
    private String appid;
    private String arg2;//通常为app_secret
    private String arg3;//其他的参数,例如新浪sdk有一个redirect_url

    public ConfigEntity(String appid) {
        this.appid = appid;
    }


    public ConfigEntity(String appid, String arg2) {
        this.appid = appid;
        this.arg2 = arg2;
    }

    public ConfigEntity(String appid, String arg2, String arg3) {
        this.appid = appid;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    public String getAppid() {
        return appid;
    }

    public String getArg2() {
        return arg2;
    }

    public String getArg3() {
        return arg3;
    }
}
