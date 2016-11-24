package com.stayfprod.github.event;

import com.stayfprod.github.model.SearchItem;

import java.util.List;

public class SearchEvent {

    public List<SearchItem> items;
    public int page;

    public SearchEvent(List<SearchItem> items, int page) {
        this.items = items;
        this.page = page;
    }
}
