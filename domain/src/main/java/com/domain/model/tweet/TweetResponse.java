package com.domain.model.tweet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TweetResponse implements Serializable {
    @SerializedName("statuses")
    @Expose
    List<Tweet> statuses;
    @SerializedName("search_metadata")
    @Expose
    SearchMetaData search_metadata;

    public TweetResponse() {
    }


    public List<Tweet> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Tweet> statuses) {
        this.statuses = statuses;
    }

    public SearchMetaData getSearch_metadata() {
        return search_metadata;
    }

    public void setSearch_metadata(SearchMetaData search_metadata) {
        this.search_metadata = search_metadata;
    }
}
