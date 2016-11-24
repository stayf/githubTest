package com.stayfprod.github.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerViewLazyScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager mLayoutManager;
    private Runnable mOnLoadTask;
    private Context mContext;

    public RecyclerViewLazyScrollListener(Context context, LinearLayoutManager layoutManager, Runnable onLoadTask) {
        mLayoutManager = layoutManager;
        mOnLoadTask = onLoadTask;
        mContext = context;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

        if (isNeedDownloadMore()) {
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            if ((visibleItemCount + firstVisibleItem) >= totalItemCount * 0.7) {
                mOnLoadTask.run();
            }
        }
    }

    public abstract boolean isNeedDownloadMore();
}
