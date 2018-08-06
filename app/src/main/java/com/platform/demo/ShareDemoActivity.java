package com.platform.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.platform.app.config.PlatformType;
import com.platform.app.share.ShareParams;
import com.platform.app.share.ShareUtil;
import com.platform.app.share.util.ShareObservable;
import com.platform.app.share.util.ShareObserver;
import com.platform.sdk.wx.share.WXShareTool;

public class ShareDemoActivity extends AppCompatActivity implements ShareObserver{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_demo);
        //注册用于分享回调的监听
        ShareObservable.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareObservable.getInstance().unRegister(this);
    }

    /**
     * 普通分享  目前仅支持  微信，qq，微博3种分享
     *
     * @param v
     */
    public void normalShare(View v) {
        ShareParams params = new ShareParams.Builder()
                .setTitle("测试")
                .setText("测试内容")
                .setUrl("http://www.baidu.com")
                .setSimpleImage("http://p83nf214c.bkt.clouddn.com/1525416286.jpg")
                .build();

        ShareUtil.doShare(this, params);
    }


    /**
     * 分享小程序卡片
     *
     * @param v
     */
    public void shareMiniProgram(View v) {
        ShareParams params = new ShareParams.Builder()
                .setTitle("测试")
                .setText("测试内容")
                .setUrl("http://www.baidu.com")
                .setPagePath("pages/index")
                .setSimpleImage("http://p83nf214c.bkt.clouddn.com/1525416286.jpg")
                .build();
        new WXShareTool().shareMiniProgram(params);
    }

    /**
     * 打开小程序
     *
     * @param v
     */
    public void openMiniProgram(View v) {
        //path填写要打开的小程序页面
        new WXShareTool().launchMiniProgram("pages/index");
    }

    @Override
    public void onShareSuccess() {

    }

    @Override
    public void onShareFailed(Enum<PlatformType> typeEnum, String message) {

    }
}
