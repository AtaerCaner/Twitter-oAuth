package com.domain.presenters.subscribers;


import com.domain.views.BaseInteractionView;

public abstract class BaseInteractionSubscriber<View extends BaseInteractionView, T> extends BaseUseCaseSubscriber<T> {
    protected final View view;

    public BaseInteractionSubscriber(View view) {
        this.view = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onError(Throwable e) {

    }
}