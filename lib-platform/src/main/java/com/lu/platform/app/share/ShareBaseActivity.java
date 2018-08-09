package com.lu.platform.app.share;

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


import com.lu.platform.R;
import com.lu.platform.app.config.Constants;
import com.lu.platform.app.config.PlatformConfigurator;
import com.lu.platform.app.util.PlatformBaseDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: luqihua
 * @Time: 2018/5/24
 * @Description: ShareBaseActivity  分享的选择页面
 */

public class ShareBaseActivity extends PlatformBaseDialog implements ShareObserver,ShareChannelAdapter.OnChannelClickListener {

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
    protected int getLayoutId() {
        return R.layout.activity_share_base;
    }

    @Override
    protected void handlerIntent(Intent intent) {
        mParams = intent.getParcelableExtra(INTENT_PARAMS);
        if (mParams == null) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mShareDelegate = new ShareDelegate(this, mParams);
        ShareObservable.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareObservable.getInstance().unRegister(this);
    }

    @Override
    protected void initView() {
        rvChannelListV = findViewById(R.id.rv_share_channel_list);
        rvChannelListV.setLayoutManager(new GridLayoutManager(this, 4));
        rvChannelListV.setNestedScrollingEnabled(false);
        mAdapter = new ShareChannelAdapter(this, getChannelData(), this);
        rvChannelListV.setAdapter(mAdapter);
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

    private List<ShareChannelBean> getChannelData() {
        ArrayList<ShareChannelBean> channelBeans = new ArrayList<>();
        if (!TextUtils.isEmpty(mParams.mReloadPath)) {
            channelBeans.add(new ShareChannelBean(Constants.SEND_PYQ, R.drawable.icon_multi_pic));
        }
        if (PlatformConfigurator.getInstance().hasConfig(Constants.WEIXIN)) {
            channelBeans.add(new ShareChannelBean(Constants.WX_FRIEND, R.drawable.icon_wechat));
            channelBeans.add(new ShareChannelBean(Constants.WX_ZONE, R.drawable.icon_wxcircle));
        }
        if (PlatformConfigurator.getInstance().hasConfig(Constants.QQ)) {
            channelBeans.add(new ShareChannelBean(Constants.QQ_FRIEND, R.drawable.icon_qq));
            channelBeans.add(new ShareChannelBean(Constants.QQ_ZONE, R.drawable.icon_q_zone));
        }

        if (PlatformConfigurator.getInstance().hasConfig(Constants.SINA)) {
            channelBeans.add(new ShareChannelBean(Constants.SINA_WEIBO, R.drawable.icon_sina));
        }
        channelBeans.add(new ShareChannelBean(Constants.COPY_LINK, R.drawable.icon_copy));
        return channelBeans;
    }

    @Override
    public void onClick(ShareChannelBean channelBean) {
        final String channelName = channelBean.getChannelName();
        if (channelName.equals(Constants.WX_ZONE)) {
            mShareDelegate.shareWXCircle();
        } else if (channelName.equals(Constants.WX_FRIEND)) {
            mShareDelegate.shareWechat();
        } else if (channelName.equals(Constants.QQ_FRIEND)) {
            mShareDelegate.shareQQ();
        } else if (channelName.equals(Constants.QQ_ZONE)) {
            mShareDelegate.shareQZone();
        } else if (channelName.equals(Constants.COPY_LINK)) {
            mShareDelegate.copyLink();
        } else if (channelName.equals(Constants.SINA_WEIBO)) {
            mShareDelegate.shareSinaWB();
        } else {
            reload(channelBean);
        }
        finish();
    }

    /**
     * 重定向到新的页面
     *
     * @param channelBean
     */
    protected void reload(ShareChannelBean channelBean) {
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
    public void onShareFailed(String typeEnum, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
        getWindow().getDecorView().setVisibility(View.GONE);
        overridePendingTransition(0, 0);
    }
}
