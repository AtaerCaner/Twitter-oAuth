package com.domain.views;


import com.domain.model.tweet.TweetResponse;


public interface SearchTweetView extends BaseInteractionView {
    void onSearchResponse(TweetResponse response);

    void onError(Throwable throwable);
}
