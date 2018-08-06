package com.platform.demo;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.platform.demo.util.FileUtil;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;

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


    public void multipleShare(View v) {

        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("data", "这是分享的文字");//这个值会插入到第一个item
        manager.setPrimaryClip(clipData);
        Toast.makeText(this, "分享内容已复制到剪切板", Toast.LENGTH_SHORT).show();
        try {
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

            Intent intent = new Intent();
            ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
            intent.putExtra("Kdescription", "分享朋友圈的图片说明");
            intent.setComponent(componentName);


            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
