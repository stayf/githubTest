package com.stayfprod.github.presenter;

import android.util.Log;

import com.stayfprod.github.App;
import com.stayfprod.github.R;
import com.stayfprod.github.api.ApiClient;
import com.stayfprod.github.event.ErrorEvent;
import com.stayfprod.github.event.SearchEvent;
import com.stayfprod.github.event.StopProgressEvent;
import com.stayfprod.github.model.SearchItem;
import com.stayfprod.github.model.SearchResponse;
import com.stayfprod.github.util.ErrorUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter extends LazyListPresenter {

    private final static String SORT_TYPE = "stars";
    private final static String ORDER_TYPE = "desc";

    public void findRepositoriesAsync(String query) {
        setNeedDownloadMore(false);

        final int remRequestNum = incrementAndGetReqNumber();

        if (query == null || query.trim().isEmpty()) {
            EventBus.getDefault().postSticky(new StopProgressEvent());
            return;
        }

        Call<SearchResponse> call = ApiClient.SERVICE.findRepositories(query, mPage, LIMIT, SORT_TYPE, ORDER_TYPE);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {

                if (getCurrentReqNumber() != remRequestNum) {
                    return;
                }

                if (!response.isSuccessful()) {
                    EventBus.getDefault().postSticky(new ErrorEvent(response.code(), ErrorUtils.parseError(response).errorMessage));
                } else {
                    EventBus.getDefault().postSticky(new SearchEvent(response.body().items, mPage));
                    incrementPage();
                }

                setNeedDownloadMore(true);
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                if (getCurrentReqNumber() != remRequestNum) {
                    return;
                }

                setNeedDownloadMore(true);
                EventBus.getDefault().postSticky(new ErrorEvent(500, App.getContext().getString(R.string.err_fail_from_server)));
            }
        });
    }
}
