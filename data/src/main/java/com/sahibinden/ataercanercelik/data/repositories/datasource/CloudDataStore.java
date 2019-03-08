package com.sahibinden.ataercanercelik.data.repositories.datasource;


import com.sahibinden.ataercanercelik.data.cache.Cache;
import com.sahibinden.ataercanercelik.data.net.RestApi;
import com.domain.model.tweet.TweetResponse;

import rx.Observable;

public class CloudDataStore implements DataStore {


    private final RestApi restApi;
    private final Cache cache;

    public CloudDataStore(RestApi restApi, Cache cache) {
        this.restApi = restApi;
        this.cache = cache;

    }


    @Override
    public Observable<TweetResponse> search(String header, String q) {
        return restApi.search(header,q);
    }

    @Override
    public Observable<TweetResponse> search(String header, String q, String maxId) {
        return restApi.search(header, q, maxId);
    }
}
