package com.lu.platform.app.share;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @Author: luqihua
 * @Time: 2018/5/24
 * @Description: 第三方分享的参数包装类
 */

public class ShareParams implements Parcelable {
    public String mTitle;//要分享的标题
    public String mText;// 要分享的摘要
    public String mDescription;//详细内容，只在自己发朋友圈时用
    public String mUrl;//点击打开的链接地址
    public String mPagePath;//微信小程序分享中的目标页面
    public String mSimpleImage;//单图的url
    public ArrayList<String> mMultiImages;//多图的url
    public String mReloadPath;//重定向页面activity的

    public ShareParams(Builder builder) {
        this.mTitle = builder.mTitle;
        this.mText = builder.mText;
        this.mUrl = builder.mUrl;
        this.mPagePath = builder.mPagePath;
        this.mSimpleImage = builder.mSimpleImage;
        this.mMultiImages = builder.mMultiImages;
        this.mReloadPath = builder.mReloadPath;
        this.mDescription = builder.mDescription;
    }


    public static class Builder {
        private String mTitle;
        private String mText;
        private String mUrl;
        private String mPagePath;
        private String mSimpleImage;
        private ArrayList<String> mMultiImages;
        private String mReloadPath;
        public String mDescription;

        public Builder() {
            this.mTitle = "";
            this.mText = "this is default text";
            this.mUrl = "";
            this.mPagePath = "";
            this.mSimpleImage = "";
            this.mMultiImages = new ArrayList<>();
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setText(String text) {
            this.mText = text;
            return this;
        }

        public Builder setUrl(String url) {
            this.mUrl = url;
            return this;
        }

        public Builder setPagePath(String path) {
            this.mPagePath = path;
            return this;
        }

        public Builder setSimpleImage(String simpleImage) {
            this.mSimpleImage = simpleImage;
            return this;
        }

        public Builder setMultiImages(ArrayList<String> multiImages) {
            this.mMultiImages = multiImages;
            return this;
        }

        public Builder setDescription(String description) {
            this.mDescription = description;
            return this;
        }

        public Builder setReloadPath(String path){
            this.mReloadPath = path;
            return this;
        }

        public ShareParams build() {
            return new ShareParams(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mText);
        dest.writeString(this.mDescription);
        dest.writeString(this.mUrl);
        dest.writeString(this.mPagePath);
        dest.writeString(this.mSimpleImage);
        dest.writeStringList(this.mMultiImages);
        dest.writeString(this.mReloadPath);
    }

    protected ShareParams(Parcel in) {
        this.mTitle = in.readString();
        this.mText = in.readString();
        this.mDescription = in.readString();
        this.mUrl = in.readString();
        this.mPagePath = in.readString();
        this.mSimpleImage = in.readString();
        this.mMultiImages = in.createStringArrayList();
        this.mReloadPath = in.readString();
    }

    public static final Creator<ShareParams> CREATOR = new Creator<ShareParams>() {
        @Override
        public ShareParams createFromParcel(Parcel source) {
            return new ShareParams(source);
        }

        @Override
        public ShareParams[] newArray(int size) {
            return new ShareParams[size];
        }
    };
}
