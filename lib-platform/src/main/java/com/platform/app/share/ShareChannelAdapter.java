package com.platform.app.share;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * author: luqihua
 * date:2018/7/30
 * description:
 **/
public class ShareChannelAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context mContext;
    private List<EChannelBean> mData;
    private int mItemPadding;
    private OnChannelClickListener mChannelClickListener;

    public ShareChannelAdapter(Context context, List<EChannelBean> data, OnChannelClickListener listener) {
        this.mContext = context;
        this.mData = data;
        this.mChannelClickListener = listener;
        initSize();
    }

    private void initSize() {
        float density = Resources.getSystem().getDisplayMetrics().density;
        mItemPadding = (int) (15 * density + 0.5f);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = new TextView(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setPadding(mItemPadding, mItemPadding, mItemPadding, mItemPadding);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(0xff313131);
        textView.setTextSize(12);
        textView.setOnClickListener(this);
        return new RecyclerView.ViewHolder(textView) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final EChannelBean itemData = mData.get(position);
        TextView itemView = (TextView) holder.itemView;

        itemView.setText(itemData.getChannelName());
        itemView.setCompoundDrawablesWithIntrinsicBounds(0,itemData.getChannelIcon(),0,0);

        itemView.setTag(itemData);
    }

    @Override
    public void onClick(View v) {
        EChannelBean bean = (EChannelBean) v.getTag();
        if (bean != null && mChannelClickListener != null) {
            mChannelClickListener.onClick(bean);
        }
    }

    public interface OnChannelClickListener {
        void onClick(Enum<EChannelBean> channelBean);

    }
}
