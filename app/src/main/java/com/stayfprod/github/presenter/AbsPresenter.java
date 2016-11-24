package com.stayfprod.github.presenter;


public abstract class AbsPresenter {
    protected static final int LIMIT = 50;
    protected final static String SORT_TYPE = "stars";
    protected final static String ORDER_TYPE = "desc";

    protected int mPage = 0;
    protected boolean mNeedDownloadMore;
    private int requestNum = 0;

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
        return requestNum;
    }

    protected int incrementAndGetReqNumber() {
        ++requestNum;
        return requestNum;
    }

    public boolean isNeedDownloadMore() {
        return mNeedDownloadMore;
    }

    public void setNeedDownloadMore(boolean needDownloadMore) {
        this.mNeedDownloadMore = needDownloadMore;
    }
}
