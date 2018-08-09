package com.platform.demo.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lu.platform.app.config.Constants;
import com.lu.platform.app.login.LoginObj;
import com.lu.platform.app.login.LoginObservable;
import com.lu.platform.app.login.LoginObserver;
import com.lu.platform.sdk.qq.login.QQLoginTool;
import com.lu.platform.sdk.wx.login.WXLoginTool;
import com.platform.demo.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginObserver {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginObservable.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginObservable.getInstance().unRegister(this);
    }

    /**
     * 注意：
     * 1.第一次登录时这些值都可以设置为null
     * 2.当第一次授权登录成功后，通过loginSuccess()方法的LoginObj参数可以获取到这些值，将这些值保存起来用于下次登录
     */

    private String openid = "1234567";
    private String accessToken = "access_token";
    private String expiresIn = "100000";//过期时间
    private String refresh_token = "refresh_token";//微信登录用于刷新登录状态的

    public void qqLogin(View v) {
        new QQLoginTool().login(this, openid, accessToken, expiresIn);
    }

    public void weixinLogin(View view) {
        new WXLoginTool().login(accessToken);
    }

    @Override
    public void loginSuccess(String platformType, LoginObj obj) {
        openid = obj.getOpenid();
        accessToken = obj.getAccess_token();
        expiresIn = obj.getExpires_in();
        if (platformType == Constants.WEIXIN) {
            refresh_token = obj.getRefresh_token();//微信登录用于刷新登录状态的
        }
        //获取到这些值之后需要保存起来，建议上传到服务器
    }

    @Override
    public void loginError(String platformType, String errMessage) {
        Toast.makeText(this, errMessage, Toast.LENGTH_SHORT).show();
    }
}

