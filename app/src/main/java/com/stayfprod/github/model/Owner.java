package com.stayfprod.github.model;

import com.google.gson.annotations.SerializedName;

public class Owner {
    public String login;

    @SerializedName("avatar_url")
    public String avatarUrl;
}
