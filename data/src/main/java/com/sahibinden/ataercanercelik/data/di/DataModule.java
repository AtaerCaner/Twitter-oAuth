package com.sahibinden.ataercanercelik.data.di;

import com.sahibinden.ataercanercelik.data.net.Service;
import com.sahibinden.ataercanercelik.data.net.ServiceGenerator;

import dagger.Module;
import dagger.Provides;


@Module
public class DataModule {

    @ActivityScope
    @Provides
    Service provideService() {
        return ServiceGenerator.createService(Service.class);
    }
}
