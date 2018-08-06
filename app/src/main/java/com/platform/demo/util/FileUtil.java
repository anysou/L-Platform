package com.platform.demo.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author luqihua
 * @time 2018/2/6 
 * @description
 */


public class FileUtil {
    /**
     * 生成一个图片文件，默认保存在根目录下的应用包名文件夹下
     */
    public static File createImageCacheFile(Context context, String dirName) {
        return createImageCacheFile(context, dirName, Bitmap.CompressFormat.JPEG);
    }

    public static File createImageCacheFile(Context context, String dirName, Bitmap.CompressFormat format) {
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory() + "/" + context.getPackageName(), dirName);
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        return new File(file, createFileName(format));
    }

    /**
     * 生成 content:// 类型uri
     */
    public static Uri fileUri2ContentUri(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
    }

    private static String createFileName(Bitmap.CompressFormat format) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'IMG'_MM_dd_HHmmss", Locale.CHINA);
        return sdf.format(date) + "." + format.toString();
    }

}
