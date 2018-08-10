package com.platform.demo.module;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lu.platform.app.share.ShareParams;
import com.lu.platform.app.share.ShareUtil;
import com.lu.platform.app.share.ShareObservable;
import com.lu.platform.app.share.ShareObserver;
import com.lu.platform.sdk.wx.share.WXShareTool;
import com.platform.demo.R;
import com.platform.demo.util.FileUtil;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;


public class ShareDemoActivity extends AppCompatActivity implements ShareObserver {

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
        ShareUtil.shareMiniProgram(params);
    }

    /**
     * 打开小程序
     *
     * @param v
     */
    public void openMiniProgram(View v) {
        //path填写要打开的小程序页面
       ShareUtil.openMiniProgram("pages/index");
    }

    /**
     * 多图分享朋友圈
     * @param v
     */
    public void senPYQ(View v) {
        try {
            //多图必须以uri的形式传递给发朋友圈界面
            ArrayList<Uri> uris = new ArrayList<>();
            for (int i = 0; i < 11; i++) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dota);
                File imageFile = FileUtil.createImageCacheFile(this, getPackageName());
                Uri uri = FileUtil.fileUri2ContentUri(this, imageFile.getAbsolutePath());
                ContentResolver contentResolver = getContentResolver();
                OutputStream outputStream = contentResolver.openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
                uris.add(uri);
            }

            new WXShareTool().sendPengYouQuan(this, "分享朋友圈的图片说明", uris);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShareSuccess() {
        //分享成功
    }

    @Override
    public void onShareFailed(String typeEnum, String message) {

    }
}
