package com.aduer.lblcus.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/1/8.
 */
public class ListViewUtils {

    private boolean canLoad;

    public int count = 10;

    private boolean enable = true;

    public void enable() {
        enable = true;
    }

    public void disable() {
        enable = false;
    }

    public void setLoadMoreListener(ListView listView, final CallBack callBack) {
        if (listView == null || callBack == null)
            return;

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!enable)
                    return;
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (canLoad) {
                        if (callBack != null) {
                            callBack.onLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!enable)
                    return;
                if (firstVisibleItem + visibleItemCount + visibleItemCount >= totalItemCount - 1 && totalItemCount >= count) {
                    canLoad = true;
                } else {
                    canLoad = false;
                }

            }
        });
    }

    public void setLoadMoreListener(GridView listView, final CallBack callBack) {
        if (listView == null || callBack == null)
            return;

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!enable)
                    return;
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (canLoad) {
                        if (callBack != null) {
                            callBack.onLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!enable)
                    return;
                if (firstVisibleItem + visibleItemCount + visibleItemCount >= totalItemCount - 1 && totalItemCount >= count) {
                    canLoad = true;
                } else {
                    canLoad = false;
                }

            }
        });
    }

    public void setLoadMoreListener(RecyclerView listView, final CallBack callBack) {
        if (listView == null || callBack == null)
            return;

        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastCount = 0;
            RecyclerView.LayoutManager manager;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (recyclerView.getAdapter().getItemCount() + 2 >= lastCount && lastCount > 0) {
                        if (callBack != null) {
                            callBack.onLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (manager == null) {
                    manager = recyclerView.getLayoutManager();
                }

                if (manager instanceof GridLayoutManager) {
                    lastCount = ((GridLayoutManager) manager).findLastVisibleItemPosition();
                } else if (manager instanceof LinearLayoutManager) {
                    lastCount = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                }
            }
        });
    }

    /**
     * @param linearLayoutManager
     * @return 返回所有item所需高度，如果高度=-1那么说明没有内容
     */
    public int getListItemTotalHeight(LinearLayoutManager linearLayoutManager, boolean hasHead, boolean hasFooter) {
        int count = linearLayoutManager.getChildCount();
        if (count < 1)
            return -1;
        int height = 0;
        View view = null;
        for (int i = 0; i < 1; i++) {
            view = linearLayoutManager.getChildAt(i);
            height += view.getHeight();
            height += linearLayoutManager.getBottomDecorationHeight(view);
            height += linearLayoutManager.getTopDecorationHeight(view);
        }
        count = linearLayoutManager.getItemCount();
        if (hasHead)
            count -= 1;
        if (hasFooter)
            count -= 1;

        height *= count;
        if (height == 0)
            return -1;
        return height;
    }

    public interface CallBack {
        void onLoadMore();
    }
}
