package com.lu.platform.app.login;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * author: luqihua
 * date:2018/8/8
 * description:
 **/
public class LoginObj implements  Parcelable {

    //======微信登录返回示例
    /**
     {
     "access_token":"ACCESS_TOKEN",
     "expires_in":7200,
     "refresh_token":"REFRESH_TOKEN",//用于刷新登录
     "openid":"OPENID",
     "scope":"SCOPE",//用户授权的作用域，使用逗号（,）分隔
     "unionid":"o6_bmasdasdsad6_2sgVt7hMZOPfL"//当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
     }
     */


    //=====QQ==返回示例
    /**
     * {
     * "ret":0,
     * "pay_token":"xxxxxxxxxxxxxxxx",
     * "pf":"openmobile_android",
     * "expires_in":"7776000",
     * "openid":"xxxxxxxxxxxxxxxxxxx",
     * "pfkey":"xxxxxxxxxxxxxxxxxxx",
     * "msg":"sucess",
     * "access_token":"xxxxxxxxxxxxxxxxxxxxx"
     * }
     */

    // 这里只解析2中登录公共的参数，openid,expires_in,access_token,
    // 另外refresh_token必须解析出来，因为微信登录中需要用它刷新登录

    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String openid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.access_token);
        dest.writeString(this.expires_in);
        dest.writeString(this.refresh_token);
        dest.writeString(this.openid);
    }

    public LoginObj() {
    }

    protected LoginObj(Parcel in) {
        this.access_token = in.readString();
        this.expires_in = in.readString();
        this.refresh_token = in.readString();
        this.openid = in.readString();
    }

    public static final Parcelable.Creator<LoginObj> CREATOR = new Parcelable.Creator<LoginObj>() {
        @Override
        public LoginObj createFromParcel(Parcel source) {
            return new LoginObj(source);
        }

        @Override
        public LoginObj[] newArray(int size) {
            return new LoginObj[size];
        }
    };
}
