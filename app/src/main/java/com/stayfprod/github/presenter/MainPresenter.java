package com.stayfprod.github.presenter;

import android.util.Log;

import com.stayfprod.github.api.ApiClient;
import com.stayfprod.github.event.ErrorEvent;
import com.stayfprod.github.event.SearchEvent;
import com.stayfprod.github.model.SearchResponse;
import com.stayfprod.github.util.ErrorUtils;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter extends AbsPresenter {

    public void findRepositoriesAsync(String query) {
        setNeedDownloadMore(false);

        final int remRequestNum = incrementAndGetReqNumber();

        Call<SearchResponse> call = ApiClient.SERVICE.findRepositories(query, mPage, LIMIT, SORT_TYPE, ORDER_TYPE);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {

                if (getCurrentReqNumber() != remRequestNum) {
                    Log.e("return", call.request().url() + ": code: " + response.code() + "remRequestNum=" + remRequestNum + ";requestNum=" + getCurrentReqNumber());
                    return;
                }

                Log.e("continue", call.request().url() + ": code: " + response.code() + "remRequestNum=" + remRequestNum + ";requestNum=" + getCurrentReqNumber());

                if (!response.isSuccessful()) {
                    EventBus.getDefault().post(new ErrorEvent(response.code(), ErrorUtils.parseError(response).errorMessage));
                } else {
                    incrementPage();
                    EventBus.getDefault().postSticky(new SearchEvent(response.body().items));
                }

                setNeedDownloadMore(true);
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                setNeedDownloadMore(true);
                EventBus.getDefault().post(new ErrorEvent(500, "Не возможно получить инфу от сервера"));
            }
        });
    }
}
