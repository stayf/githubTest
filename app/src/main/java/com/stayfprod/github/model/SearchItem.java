package com.stayfprod.github.model;

import com.google.gson.annotations.SerializedName;


public class SearchItem {

    public long id;

    public String name;

    @SerializedName("full_name")
    public String fullName;

    public String description;

    public Owner owner;
}
