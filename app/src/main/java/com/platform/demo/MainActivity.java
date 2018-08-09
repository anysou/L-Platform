package com.platform.demo;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.platform.demo.module.LoginActivity;
import com.platform.demo.module.PayDemoActivity;
import com.platform.demo.module.ShareDemoActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23)
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }

    /**
     * 测试支付
     *
     * @param v
     */
    public void testPay(View v) {
        startActivity(new Intent(this, PayDemoActivity.class));
    }

    /**
     * 测试分享
     *
     * @param v
     */
    public void testShare(View v) {
        startActivity(new Intent(this, ShareDemoActivity.class));
    }


    /**
     * 微信或者qq登录
     *
     * @param v
     */
    public void testLogin(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }

}
