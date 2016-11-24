package com.stayfprod.github.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("total_count")
    public int totalCount;

    @SerializedName("items")
    public List<SearchItem> items;
}
