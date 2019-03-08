package com.domain.repositories;

import com.domain.model.tweet.TweetResponse;

import rx.Observable;

/**
 * Created by AtaerCanerCelik on 30/10/2017.
 */

public interface SearchRepository extends BaseRepository {
    Observable<TweetResponse> search(final String header, final String q);

    Observable<TweetResponse> search(final String header, final String q, final String maxId);


}
