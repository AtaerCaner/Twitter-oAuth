package com.sahibinden.ataercanercelik.data.repositories.datasource;


import android.util.Log;

import com.sahibinden.ataercanercelik.data.cache.Cache;
import com.sahibinden.ataercanercelik.data.di.ActivityScope;
import com.sahibinden.ataercanercelik.data.net.RestApi;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

@ActivityScope
public class DataStoreFactory {
    private final String TAG = "DataStoreFactory";
    private final Cache cache;
    private final RestApi restApi;


    @Inject
    public DataStoreFactory(Cache cache, RestApi restApi) {
        this.cache = cache;
        this.restApi = restApi;
    }


    public DataStore create(String key) {
        DataStore userDataStore;

        if (!this.cache.isExpired(key) && this.cache.isCached(key)) {
            Log.d(TAG, "data retrieved from cache");
            userDataStore = new DiskDataStore(this.cache);
        } else {
            Log.d(TAG, "data retrieved from cloud");
            userDataStore = createCloudDataStore();
        }


        //userDataStore = createCloudDataStore();

        return userDataStore;
    }

    public DataStore createCloudDataStore() {
        return new CloudDataStore(restApi, this.cache);
    }

    public Observable<String> logout() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                cache.evictAllFile();
                //restApi.resetSecureService();
                subscriber.onNext("Ok");
            }
        });
    }
}
