package com.sahibinden.ataercanercelik.challenge.di;


import com.sahibinden.ataercanercelik.data.di.ActivityScope;
import com.sahibinden.ataercanercelik.challenge.ui.base.BaseActivity;
import com.sahibinden.ataercanercelik.challenge.ui.search.TweetListActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseActivity activity);

    void inject(TweetListActivity activity);


}
