package com.heysound.superstar.module.common.content.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by LongYu on 2016/3/8.
 */
public interface AdapterCallBack<VH extends RecyclerView.ViewHolder> {
    VH onCreateFooterViewHolder(ViewGroup parent, int viewType);

    VH onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    VH onCreateEmptyViewHolder(ViewGroup parent, int viewType);

    void onBindFooterView(VH vh,final int position);

    void onBindHeaderView(VH vh,final int position);

    void onBindItemView(VH vh,final int position);
}
