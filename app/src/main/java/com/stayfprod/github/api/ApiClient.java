package com.stayfprod.github.api;

import com.stayfprod.github.App;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final ApiService SERVICE;
    public static final Retrofit RETROFIT;

    static {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient)
                .baseUrl(App.API_ENDPOINT_URL)
                .addConverterFactory(GsonConverterFactory.create());
        SERVICE = (RETROFIT = builder.build()).create(ApiService.class);
    }
}
