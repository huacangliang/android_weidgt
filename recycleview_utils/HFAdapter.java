package com.heysound.superstar.module.common.content.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.heysound.superstar.util.RecycleAdapterPlu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LongYu on 2016/3/4.
 * Head And Footer Adapter with {@link android.support.v7.widget.RecyclerView.Adapter}
 */
public abstract class HFAdapter<VH extends RecyclerView.ViewHolder, T extends Object> extends RecyclerView.Adapter<VH> implements AdapterCallBack<VH>,RecycleAdapterPlu {

    private boolean hasMore;
    protected Context mContext;
    private RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mManager;
    public static final int ITEM = 1;
    public static final int FOOTER = 2;
    public static final int HEADER = 3;
    public static final int EMPTY = 4;
    private int itemMarginTop = 14;
    private int itemMarginLeft = 20;
    private int itemMarginRight = 20;
    private int itemMarginBottom = 0;

    private boolean hasFooter = true;
    private boolean hasHeader = true;
    private boolean showEmptyView = false;
    private boolean isEmptyView;

    private AdapterView.OnItemClickListener onItemClickListener;

    public boolean isHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public boolean isHasFooter() {
        return hasFooter;
    }

    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    protected List<T> datas = new ArrayList<T>(10);

    public boolean hasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getItemMarginBottom() {
        return itemMarginBottom;
    }

    public void setItemMarginBottom(int itemMarginBottom) {
        this.itemMarginBottom = itemMarginBottom;
    }

    public void setShowEmptyView(boolean showEmptyView) {
        this.showEmptyView = showEmptyView;
    }

    public int getItemMarginTop() {
        return itemMarginTop;
    }

    public void setItemMarginTop(int itemMarginTop) {
        this.itemMarginTop = itemMarginTop;
    }

    public int getItemMarginLeft() {
        return itemMarginLeft;
    }

    public void setItemMarginLeft(int itemMarginLeft) {
        this.itemMarginLeft = itemMarginLeft;
    }

    public int getItemMarginRight() {
        return itemMarginRight;
    }

    public void setItemMarginRight(int itemMarginRight) {
        this.itemMarginRight = itemMarginRight;
    }

    public RecyclerView.LayoutManager getManager() {
        return mManager;
    }

    public void setManager(RecyclerView.LayoutManager mManager) {
        this.mManager = mManager;
        this.mRecyclerView.setLayoutManager(mManager);
    }

    public boolean isEmptyView() {
        return isEmptyView;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HFAdapter(final Context mContext, RecyclerView mRecyclerView) {
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
        this.mRecyclerView.setAdapter(this);
    }

    public void initData(List<T> datas) {
        this.datas.clear();
        notifyDataSetChanged();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void loadMore(List<T> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void addItem(T datas) {
        this.datas.add(datas);
        notifyItemChanged(getItemCount());
    }

    public void removeItem(){
        //not dosemothing
    }

    @Override
    public int getItemCount() {
        int size = 0;
        size += hasHeader ? 1 : 0;
        size += hasFooter ? 1 : 0;
        if (showEmptyView && datas.size() <= 0) {
            isEmptyView = true;
            return size + 1;
        }
        isEmptyView = false;
        return this.datas.size() + size;
    }

    public boolean isFooter(int pos) {
        return pos == (getItemCount() - 1) && hasFooter;
    }

    public boolean isHeader(int pos) {
        return pos == 0 && hasHeader;
    }

    public boolean isFooterType(int type) {
        return type == FOOTER && hasFooter;
    }

    public boolean isHeaderType(int type) {
        return type == HEADER && hasHeader;
    }

    @Override
    public int getItemViewType(int position) {
        return isFooter(position) ? FOOTER : isHeader(position) ? HEADER : isEmptyView ? EMPTY : ITEM;
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (this.onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, v, position, 0);
                }
            });
        }
        if (isHeader(position)) {
            onBindHeaderView(holder,position);
        } else if (isFooter(position)) {
            onBindFooterView(holder,position);
        } else {
            onBindItemView(holder,position);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            return onCreateFooterViewHolder(parent, viewType);
        } else if (viewType == ITEM) {
            return onCreateItemViewHolder(parent, viewType);
        } else if (isEmptyView) {
            return onCreateEmptyViewHolder(parent, viewType);
        } else {
            return onCreateHeaderViewHolder(parent, viewType);
        }
    }

    public VH onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public VH onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public VH onCreateEmptyViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindFooterView(VH vh, int position) {

    }

    @Override
    public void onBindHeaderView(VH vh, int position) {

    }

    @Override
    public void onBindItemView(VH vh, int position) {

    }

    @Override
    public <V> V getItem(int position) {
        return (V) datas.get(position);
    }
}
