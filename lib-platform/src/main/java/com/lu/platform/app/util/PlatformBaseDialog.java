package com.lu.platform.app.util;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.lu.platform.R;

/**
 * author: luqihua
 * date:2018/8/9
 * description:
 **/
public abstract class PlatformBaseDialog extends AppCompatActivity{
    private static final int ANIMATE_DURATION = 200;
    public static final String INTENT_PARAMS = "params";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.share_AppTheme);
        if (isAndroid21()) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(getLayoutId());

        if (isAndroid21()) {
            Transition transition = new Fade();
            transition.setDuration(ANIMATE_DURATION);
            //进入的动画
            getWindow().setEnterTransition(transition);

            Transition transition2 = new Fade();
            transition2.setDuration(ANIMATE_DURATION);
            //退出的动画  这个只有按钮返回键的时候才起作用
            getWindow().setReturnTransition(transition2);
        }
        handlerIntent(getIntent());
        initView();
    }

    protected abstract int getLayoutId();
    protected abstract void handlerIntent(Intent intent);

    protected abstract void initView();


    public void back(View v) {
        if (isAndroid21()) {
            //模拟转场动画
            View decorView = getWindow().getDecorView();
            ViewCompat.animate(decorView)
                    .alpha(0)
                    .setDuration(ANIMATE_DURATION)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    })
                    .start();
        } else {
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isAndroid21() && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                back(null);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    protected static boolean isAndroid21() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

}
