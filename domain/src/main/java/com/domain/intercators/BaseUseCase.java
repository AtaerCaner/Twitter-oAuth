package com.domain.intercators;

import com.domain.repositories.BaseRepository;
import com.domain.schedulers.ObserveOn;
import com.domain.schedulers.SubscribeOn;


import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;


public abstract class BaseUseCase<T> implements UseCase {
    protected final BaseRepository baseRepository;
    private final SubscribeOn subscribeOn;
    private final ObserveOn observeOn;
    private Subscription subscription = Subscriptions.empty();

    protected BaseUseCase(BaseRepository BaseRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        this.baseRepository = BaseRepository;
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
    }

    public void execute(Subscriber subscriber) {
        subscription = buildUseCaseObservable().subscribeOn(subscribeOn.getScheduler())
                .observeOn(observeOn.getScheduler())
                .subscribe(subscriber);
    }

    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    protected abstract Observable<T> buildUseCaseObservable();
}
