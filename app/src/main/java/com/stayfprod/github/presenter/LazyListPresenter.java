package com.stayfprod.github.presenter;


public abstract class LazyListPresenter {
    static final int LIMIT = 50;

    protected int mPage = 0;
    protected boolean mNeedDownloadMore;
    private int mRequestNum = 0;

    protected void incrementPage() {
        this.mPage++;
    }

    public int getPage() {
        return mPage;
    }

    public boolean isFirstPage() {
        return mPage == 0;
    }

    public void cleanPage() {
        mPage = 0;
    }

    protected int getCurrentReqNumber() {
        return mRequestNum;
    }

    protected int incrementAndGetReqNumber() {
        ++mRequestNum;
        return mRequestNum;
    }

    public boolean isNeedDownloadMore() {
        return mNeedDownloadMore;
    }

    protected void setNeedDownloadMore(boolean needDownloadMore) {
        this.mNeedDownloadMore = needDownloadMore;
    }
}
