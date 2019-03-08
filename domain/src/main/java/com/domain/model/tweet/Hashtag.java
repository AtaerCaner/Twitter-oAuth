package com.domain.model.tweet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Hashtag implements Serializable{
    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("indices")
    @Expose
    private List<Integer> indices;

    public String getText() {
        return text;
    }

    public List<Integer> getIndices() {
        return indices;
    }
}
