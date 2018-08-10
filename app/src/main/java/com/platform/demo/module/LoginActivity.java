package com.platform.demo.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lu.platform.app.config.Constants;
import com.lu.platform.app.login.LoginObj;
import com.lu.platform.app.login.LoginObservable;
import com.lu.platform.app.login.LoginObserver;
import com.lu.platform.app.login.LoginUtil;
import com.lu.platform.sdk.qq.login.QQLoginTool;
import com.lu.platform.sdk.wx.login.WXLoginTool;
import com.platform.demo.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginObserver {


    private LoginObj loginObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginObservable.getInstance().register(this);
        loginObj = new LoginObj();
        /**
         * 注意：
         * 1.第一次登录时这些值都可以设置为null
         * 2.当第一次授权登录成功后，通过loginSuccess()方法的LoginObj参数可以获取到这些值，将这些值保存起来用于下次登录
         */

        loginObj.setOpenid("1234567");
        loginObj.setAccess_token("access_token");
        loginObj.setExpires_in("100000");//过期时间
        loginObj.setRefresh_token("refresh_token");//微信登录用于刷新登录状态的
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginObservable.getInstance().unRegister(this);
    }



    public void qqLogin(View v) {
        LoginUtil.doLogin(this,loginObj);
    }

    public void weixinLogin(View view) {
        LoginUtil.doLogin(this,loginObj);
    }

    @Override
    public void loginSuccess(String platformType, LoginObj obj) {
        this.loginObj = obj;
        //获取到这些值之后需要保存起来，建议上传到服务器
    }

    @Override
    public void loginError(String platformType, String errMessage) {
        Toast.makeText(this, errMessage, Toast.LENGTH_SHORT).show();
    }
}

