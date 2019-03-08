package com.sahibinden.ataercanercelik.data.repositories.datasource;

import com.domain.model.tweet.TweetResponse;

import rx.Observable;


public interface DataStore {

    Observable<TweetResponse> search(final String header, final String q);

    Observable<TweetResponse> search(final String header, final String q, final String maxId);


}
