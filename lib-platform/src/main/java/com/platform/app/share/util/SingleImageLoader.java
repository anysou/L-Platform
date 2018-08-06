package com.platform.app.share.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: luqihua
 * @Time: 2018/5/24
 * @Description: BitmapLoader
 */

public class SingleImageLoader {
    //加载图片
    public static void loadBitmap(String path, LoadCallback callback) {
        if (path == null || path.length() == 0) {
            callback.onLoadSuccess(null);
        } else if (path.startsWith("http")) {
            loadBitmapFromNetwork(path, callback);
        } else {
            loadBitmapFromFile(path, callback);
        }
    }

    /**
     * 加载本地图片
     *
     * @param path
     * @param callback
     */
    private static void loadBitmapFromFile(String path, LoadCallback callback) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (path.endsWith("jpeg") || path.endsWith("jpg") || path.endsWith("png")) {
            callback.onLoadSuccess(bitmap);
        } else {
            callback.onError("微信分享图片必须是jpeg/jpg或者png格式");
        }
    }

    /**
     * 加载网络图片
     *
     * @param path
     * @param callback
     */
    private static void loadBitmapFromNetwork(String path, LoadCallback callback) {
        new Thread(new LoadTask(path, new LoadHandler(Looper.myLooper(), callback))).start();
    }

    /*==========================================================================================*/

    private static class LoadTask implements Runnable {

        private final String mPath;
        private final Handler mHandler;

        private LoadTask(String path, Handler handler) {
            this.mPath = path;
            this.mHandler = handler;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            //要返回的bitmap数据  初始化为空白
            try {
                URL url = new URL(mPath);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                int code = conn.getResponseCode();
                if (code == 200) {
                    InputStream in = conn.getInputStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    BitmapFactory.decodeStream(in, null, options);
//
//
//                    int width = options.outWidth;
//                    int height = options.outHeight;
                    Rect rect = new Rect(0,0,250,250);
//                    if (width > 250 && height > 250) {
//                        rect = new Rect(width / 2 - 125, height / 2 - 125, width / 2 + 125, height / 2 + 125);
//                    }else {
//                        rect = new Rect(0, 0, width, height);
//                    }
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(in, false);

                    Bitmap bitmap = bitmapRegionDecoder.decodeRegion(rect, options);
                    in.close();
                    //将数据发送到主线程handler处理
                    mHandler.obtainMessage(LoadHandler.HANDLER_SUCCESS, bitmap).sendToTarget();
                } else {
                    mHandler.obtainMessage(LoadHandler.HANDLER_ERROR, "BitmapLoader：图片加载出错").sendToTarget();
                }
            } catch (IOException e) {
                //将数据返回
                mHandler.obtainMessage(LoadHandler.HANDLER_ERROR, "BitmapLoader：图片加载网络连接错误").sendToTarget();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
        }
    }


    /*==============================================================================*/

    private static class LoadHandler extends Handler {
        private static final int HANDLER_SUCCESS = 100;
        private static final int HANDLER_ERROR = 101;

        private final WeakReference<LoadCallback> callbackWeakReference;

        private LoadHandler(Looper looper, LoadCallback callback) {
            super(looper);
            this.callbackWeakReference = new WeakReference<>(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            LoadCallback callback = callbackWeakReference.get();
            if (callback == null) return;
            switch (msg.what) {
                case HANDLER_SUCCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    callback.onLoadSuccess(bitmap);
                    break;
                case HANDLER_ERROR:
                    String message = (String) msg.obj;
                    callback.onError(message);
                    break;
            }
        }
    }


    /*=========================================================*/

    public interface LoadCallback {
        void onLoadSuccess(Bitmap bitmap);

        void onError(String message);
    }
}
