
package com.domain.intercators;


import com.domain.model.tweet.TweetResponse;
import com.domain.repositories.SearchRepository;
import com.domain.schedulers.ObserveOn;
import com.domain.schedulers.SubscribeOn;
import com.domain.views.SearchTweetView;
import com.domain.views.View;

import javax.inject.Inject;

import rx.Observable;

public class SearchUseCase extends BaseUseCase<TweetResponse> {
    private SearchTweetView view;
    private String header, q, maxId;


    @Inject
    public SearchUseCase(SearchRepository repository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(repository, subscribeOn, observeOn);
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public void setMaxId(Long maxId) {
        if (maxId == null)
            this.maxId = null;
        else
            this.maxId = String.valueOf(maxId);
    }

    @Override
    protected Observable<TweetResponse> buildUseCaseObservable() {
        Observable<TweetResponse> tweetResponseObservable;
        if (maxId == null)
            tweetResponseObservable = ((SearchRepository) baseRepository).search(header, q);
        else
            tweetResponseObservable = ((SearchRepository) baseRepository).search(header, q, maxId);

        return tweetResponseObservable;
    }

    @Override
    public void attachView(View view) {
        this.view = (SearchTweetView) view;
    }

    public SearchTweetView getView() {
        return view;
    }

}
