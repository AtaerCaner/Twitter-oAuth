package com.sahibinden.ataercanercelik.challenge.di;


import android.content.Context;

import com.sahibinden.ataercanercelik.data.cache.Cache;
import com.sahibinden.ataercanercelik.data.cache.CacheImpl;
import com.sahibinden.ataercanercelik.data.di.ActivityScope;
import com.sahibinden.ataercanercelik.data.di.DataModule;
import com.sahibinden.ataercanercelik.data.executor.JobExecutor;
import com.sahibinden.ataercanercelik.data.repositories.SearchDataRepository;
import com.sahibinden.ataercanercelik.challenge.TwitterSearchApplication;
import com.sahibinden.ataercanercelik.challenge.UIThread;
import com.domain.executor.PostExecutionThread;
import com.domain.executor.ThreadExecutor;
import com.domain.repositories.SearchRepository;
import com.domain.schedulers.ObserveOn;
import com.domain.schedulers.SubscribeOn;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ActivityScope
@Module(includes = DataModule.class)
public class ApplicationModule {
    private final TwitterSearchApplication application;
    private final String token;

    public ApplicationModule(TwitterSearchApplication application, String token) {
        this.application = application;
        this.token = token;
    }

    @Provides
    @ActivityScope
    String provideToken() {
        return token;
    }


    @Provides
    @ActivityScope
    Context provideApplicationContext() {
        return application;
    }


    @ActivityScope
    @Provides
    public Picasso providePicasso() {
        return new Picasso.Builder(application).build();
    }

    @ActivityScope
    @Provides
    SubscribeOn provideSubscribeOn() {
        return (new SubscribeOn() {
            @Override
            public Scheduler getScheduler() {
                return Schedulers.newThread();
            }
        });
    }

    @ActivityScope
    @Provides
    ObserveOn provideObserveOn() {
        return (new ObserveOn() {
            @Override
            public Scheduler getScheduler() {
                return AndroidSchedulers.mainThread();
            }
        });
    }


    @Provides
    @ActivityScope
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @ActivityScope
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @ActivityScope
    Cache provideCacheImpl(CacheImpl cache) {
        return cache;
    }

    @Provides
    @ActivityScope
    public SearchRepository provideSearchDataRepository(SearchDataRepository repository) {
        return repository;
    }
}

