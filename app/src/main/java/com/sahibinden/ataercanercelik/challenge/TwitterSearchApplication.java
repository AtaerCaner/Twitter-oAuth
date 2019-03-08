package com.sahibinden.ataercanercelik.challenge;

import android.app.Application;
import android.support.multidex.MultiDex;
import com.sahibinden.ataercanercelik.challenge.di.ApplicationComponent;
import com.sahibinden.ataercanercelik.challenge.di.ApplicationModule;

import static com.sahibinden.ataercanercelik.challenge.di.DaggerApplicationComponent.builder;

public class TwitterSearchApplication extends Application {
    private ApplicationComponent applicationComponent;
    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public void initializeInjector(String token) {
        this.applicationComponent = builder()
                .applicationModule(new ApplicationModule(this, token))
                .build();

    }


    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        initializeInjector("");

    }
}
