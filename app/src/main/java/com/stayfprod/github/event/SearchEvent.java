package com.stayfprod.github.event;

import com.stayfprod.github.model.SearchItem;

import java.util.List;

public class SearchEvent {

    public List<SearchItem> items;

    public SearchEvent(List<SearchItem> items) {
        this.items = items;
    }

}
