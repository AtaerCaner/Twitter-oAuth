
package com.domain.model.tweet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Url_ implements Serializable {

    @SerializedName("urls")
    @Expose
    private List<Url__> urls = null;

    public List<Url__> getUrls() {
        return urls;
    }

    public void setUrls(List<Url__> urls) {
        this.urls = urls;
    }

}
