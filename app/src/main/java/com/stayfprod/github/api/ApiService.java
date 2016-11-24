package com.stayfprod.github.api;

import com.stayfprod.github.model.SearchResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("search/repositories")
    Call<SearchResponse> findRepositories(@Query("q") String q, @Query("page") Integer page, @Query("per_page") Integer perPage, @Query("sort") String sort, @Query("order") String order);
}
