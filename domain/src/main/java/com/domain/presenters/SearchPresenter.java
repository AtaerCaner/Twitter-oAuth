package com.domain.presenters;

import com.domain.intercators.SearchUseCase;
import com.domain.model.tweet.TweetResponse;
import com.domain.presenters.subscribers.BaseInteractionSubscriber;
import com.domain.views.SearchTweetView;

import javax.inject.Inject;

public class SearchPresenter implements Presenter {
    private final SearchUseCase useCase;


    public void attachGetTweetListView(SearchTweetView view) {
        useCase.attachView(view);
    }

    @Inject
    public SearchPresenter(SearchUseCase useCase) {
        this.useCase = useCase;
    }

    public void search(final String header, final String q) {
        search(header, q, null);
    }

    public void search(final String header, final String q, final Long maxId) {

        useCase.setHeader(header);
        useCase.setQ(q);
        useCase.setMaxId(maxId);

        useCase.execute(new BaseInteractionSubscriber<SearchTweetView, TweetResponse>(useCase.getView()) {
            @Override
            public void onNext(TweetResponse response) {


                SearchPresenter.this.useCase.getView().onSearchResponse(response);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                SearchPresenter.this.useCase.getView().onError(e);
            }
        });
    }


    @Override
    public void destroy() {
        useCase.unsubscribe();
    }
}
