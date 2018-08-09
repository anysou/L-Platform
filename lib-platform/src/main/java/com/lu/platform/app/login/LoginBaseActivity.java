package com.lu.platform.app.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Transition;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.lu.platform.R;
import com.lu.platform.app.config.Constants;
import com.lu.platform.app.config.PlatformConfigurator;
import com.lu.platform.app.share.ShareChannelAdapter;
import com.lu.platform.app.share.ShareChannelBean;
import com.lu.platform.app.share.ShareParams;
import com.lu.platform.app.util.PlatformBaseDialog;
import com.lu.platform.sdk.qq.login.QQLoginTool;
import com.lu.platform.sdk.wx.login.WXLoginTool;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: luqihua
 * @Time: 2018/5/24
 * @Description: ShareBaseActivity  分享的选择页面
 */

public class LoginBaseActivity extends PlatformBaseDialog implements View.OnClickListener {


    private TextView tvWXLoginV, tvQQLoginV;
    private LoginObj mLoginObj;

    private WXLoginTool mWxLoginTool;
    private QQLoginTool mQqLoginTool;

    protected static void launch(Activity activity, ShareParams params) {
        Intent intent = new Intent(activity, LoginBaseActivity.class);
        intent.putExtra(INTENT_PARAMS, params);
        if (isAndroid21()) {
            //android 5.0+使用转场动画
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
            ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
        } else {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_base;
    }

    @Override
    protected void handlerIntent(Intent intent) {
        mLoginObj = intent.getParcelableExtra(INTENT_PARAMS);
        if (mLoginObj == null) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    protected void initView() {
        tvQQLoginV = findViewById(R.id.tv_qq);
        tvWXLoginV = findViewById(R.id.tv_weixin);

        tvQQLoginV.setOnClickListener(this);
        tvWXLoginV.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mQqLoginTool != null) {
            mQqLoginTool.onActivityResult(requestCode, resultCode, data);
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_qq) {
            doQQLogin();
        } else if (v.getId() == R.id.tv_weixin) {
            doWXLogin();
        }
    }

    /**
     * qq登录
     */
    private void doQQLogin() {
        if (mQqLoginTool == null) {
            mQqLoginTool = new QQLoginTool();
        }
        mQqLoginTool.login(this, mLoginObj.getOpenid(), mLoginObj.getAccess_token(), mLoginObj.getExpires_in());
    }

    /**
     * 微信登录
     */
    private void doWXLogin() {
        if (mWxLoginTool == null) {
            mWxLoginTool = new WXLoginTool();
        }
        mWxLoginTool.login(mLoginObj.getRefresh_token());
        finish();
    }

}
