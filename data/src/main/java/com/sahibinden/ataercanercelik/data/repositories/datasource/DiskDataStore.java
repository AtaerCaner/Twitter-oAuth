package com.sahibinden.ataercanercelik.data.repositories.datasource;


import com.sahibinden.ataercanercelik.data.cache.Cache;
import com.domain.model.tweet.TweetResponse;

import rx.Observable;


public class DiskDataStore implements DataStore {

    private final Cache cache;

    public DiskDataStore(Cache cache) {
        this.cache = cache;
    }


    @Override
    public Observable<TweetResponse> search(String header, String q) {
        return null;
    }

    @Override
    public Observable<TweetResponse> search(String header, String q, String maxId) {
        return null;
    }
}

