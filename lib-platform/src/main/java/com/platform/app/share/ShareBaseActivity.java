package com.platform.app.share;

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
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Transition;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.platform.app.config.PlatformType;
import com.platform.app.share.util.ShareObservable;
import com.platform.app.share.util.ShareObserver;

import java.util.ArrayList;
import java.util.List;

import lu.lib_alipay.R;

/**
 * @Author: luqihua
 * @Time: 2018/5/24
 * @Description: ShareBaseActivity  分享的选择页面
 */

public class ShareBaseActivity extends AppCompatActivity implements ShareObserver, ShareChannelAdapter.OnChannelClickListener {

    private static final int ANIMATE_DURATION = 200;
    public static final String INTENT_PARAMS = "params";

    protected ShareParams mParams;
    private RecyclerView rvChannelListV;
    /**
     * 执行分享的代理类
     */
    private ShareDelegate mShareDelegate;
    private ShareChannelAdapter mAdapter;

    /**
     * 启动类
     *
     * @param activity
     */
    protected static void launch(Activity activity, ShareParams params) {
        Intent intent = new Intent(activity, ShareBaseActivity.class);
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.share_AppTheme);
        if (isAndroid21()) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_custom_share);

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
        mShareDelegate = new ShareDelegate(this, mParams);
        ShareObservable.getInstance().register(this);
    }

    private void handlerIntent(Intent intent) {
        mParams = intent.getParcelableExtra(INTENT_PARAMS);
        if (mParams == null) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        ShareObservable.getInstance().unRegister(this);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mShareDelegate != null)
            mShareDelegate.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mShareDelegate != null)
            mShareDelegate.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        rvChannelListV = findViewById(R.id.rv_share_channel_list);
        rvChannelListV.setLayoutManager(new GridLayoutManager(this, 4));
        rvChannelListV.setNestedScrollingEnabled(false);
        mAdapter = new ShareChannelAdapter(this, getChannelData(), this);
        rvChannelListV.setAdapter(mAdapter);
    }

    private List<EChannelBean> getChannelData() {
        ArrayList<EChannelBean> channelBeans = new ArrayList<>();
        if (!TextUtils.isEmpty(mParams.mReloadPath)) {
            channelBeans.add(EChannelBean.WX_SEND_PYQ);
        }
        channelBeans.add(EChannelBean.WX_HY);
        channelBeans.add(EChannelBean.WX_PYQ);
        channelBeans.add(EChannelBean.QQ_HY);
        channelBeans.add(EChannelBean.QQ_ZONE);
        channelBeans.add(EChannelBean.COPY);
        return channelBeans;
    }

    /*====================================================*/

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


    private static boolean isAndroid21() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    @Override
    public void onClick(Enum<EChannelBean> channelBean) {
        if (channelBean==EChannelBean.WX_PYQ) {
            mShareDelegate.shareWXCircle();
        } else if (channelBean==EChannelBean.WX_HY) {
            mShareDelegate.shareWechat();
        } else if (channelBean==EChannelBean.QQ_HY) {
            mShareDelegate.shareQQ();
        } else if (channelBean==EChannelBean.QQ_ZONE) {
            mShareDelegate.shareQZone();
        } else if (channelBean==EChannelBean.COPY) {
            mShareDelegate.copyLink();
        } else if (channelBean==EChannelBean.SINA_WB) {
            mShareDelegate.shareSinaWB();
        } else {
            reload(channelBean);
        }
    }

    /**
     * 重定向到新的页面
     * @param channelBean
     */
    protected void reload(Enum<EChannelBean> channelBean) {
        if (TextUtils.isEmpty(mParams.mReloadPath)) return;
        Intent intent = new Intent();
        intent.setClassName(this, mParams.mReloadPath);
        intent.putExtra(INTENT_PARAMS, mParams);
        startActivity(intent);
        finish();
    }



    @Override
    public void onShareSuccess() {
        Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
        finish();
        getWindow().getDecorView().setVisibility(View.GONE);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onShareFailed(Enum<PlatformType> typeEnum, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
        getWindow().getDecorView().setVisibility(View.GONE);
        overridePendingTransition(0, 0);
    }
}
