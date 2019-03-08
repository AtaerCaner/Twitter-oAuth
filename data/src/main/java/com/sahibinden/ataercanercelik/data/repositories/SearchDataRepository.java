package com.sahibinden.ataercanercelik.data.repositories;

import com.sahibinden.ataercanercelik.data.repositories.datasource.DataStoreFactory;
import com.domain.model.tweet.TweetResponse;
import com.domain.repositories.SearchRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by AtaerCanerCelik on 30/10/2017.
 */

public class SearchDataRepository implements SearchRepository {
    private final DataStoreFactory dataStoreFactory;

    @Inject
    public SearchDataRepository(DataStoreFactory dataStoreFactory) {
        this.dataStoreFactory = dataStoreFactory;
    }

    @Override
    public Observable<TweetResponse> search(String header, String q) {
        return dataStoreFactory.createCloudDataStore().search(header, q);
    }

    @Override
    public Observable<TweetResponse> search(String header, String q, String maxId) {
        return dataStoreFactory.createCloudDataStore().search(header, q, maxId);
    }
}
