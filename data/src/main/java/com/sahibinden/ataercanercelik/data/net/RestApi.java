package com.sahibinden.ataercanercelik.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sahibinden.ataercanercelik.data.cache.serializer.JsonSerializer;
import com.sahibinden.ataercanercelik.data.exception.NetworkConnectionException;
import com.sahibinden.ataercanercelik.data.exception.error403.AuthenticationException;
import com.sahibinden.ataercanercelik.data.exception.error404.BadRequestException;
import com.sahibinden.ataercanercelik.data.exception.error500.ServerException;
import com.domain.model.ErrorResponse;
import com.domain.model.tweet.TweetResponse;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

public class RestApi {
    final String TAG = "RestApi";
    private final Context context;
    public static Service service = null;
    @Inject
    JsonSerializer serializer;


    //region Initialize
    @Inject
    public RestApi(Context context, Service endpoints) {
        this.context = context;
        this.service = endpoints;
    }

    private boolean isThereAnyError(Response response, Subscriber subscriber) {
        boolean isError = false;


        if (response == null) {
            subscriber.onError(new ServerException("Server Error"));
            return true;
        }

        if (response.errorBody() == null)
            return isError;

        ErrorResponse errorResponse = null;
        try {
            errorResponse = serializer.deserializeError(new String(response.errorBody().bytes()));


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errorResponse == null) {
            subscriber.onError(new ServerException("Server Error"));
            return true;
        }


        switch (response.code()) {
            case 400:
                isError = true;
                subscriber.onError(new BadRequestException());
                break;
            case 404:
                isError = true;
                subscriber.onError(new BadRequestException());
                break;
            case 401:
                isError = true;
                subscriber.onError(new AuthenticationException());
                break;
            case 500:
                isError = true;
                subscriber.onError(new ServerException());
                break;
        }


        return isError;
    }

    private boolean isThereInternetConnection(Subscriber subscriber) {
        boolean isConnected;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        if (!isConnected)
            subscriber.onError(new NetworkConnectionException("There are no internet connection !"));

        return isConnected;
    }
    // endregion

    //region Methods
    public Observable<TweetResponse> search(final String header, final String q) {
        return Observable.create(new Observable.OnSubscribe<TweetResponse>() {
            @Override
            public void call(final Subscriber<? super TweetResponse> subscriber) {
                if (!isThereInternetConnection(subscriber))
                    return;

                try {

                    service.search(header, q).enqueue(new Callback<TweetResponse>() {
                        @Override
                        public void onResponse(Call<TweetResponse> call, Response<TweetResponse> response) {
                            if (isThereAnyError(response, subscriber))
                                return;

                            subscriber.onNext(response.body());
                        }

                        @Override
                        public void onFailure(Call<TweetResponse> call, Throwable t) {
                            subscriber.onError(new NetworkConnectionException(t.getCause()));
                        }
                    });


                } catch (Exception e) {
                    subscriber.onError(new NetworkConnectionException(e.getCause()));
                }
            }
        });
    }
    public Observable<TweetResponse> search(final String header, final String q, final String maxId) {
        return Observable.create(new Observable.OnSubscribe<TweetResponse>() {
            @Override
            public void call(final Subscriber<? super TweetResponse> subscriber) {
                if (!isThereInternetConnection(subscriber))
                    return;

                try {

                    service.search(header, q, maxId).enqueue(new Callback<TweetResponse>() {
                        @Override
                        public void onResponse(Call<TweetResponse> call, Response<TweetResponse> response) {
                            if (isThereAnyError(response, subscriber))
                                return;

                            subscriber.onNext(response.body());
                        }

                        @Override
                        public void onFailure(Call<TweetResponse> call, Throwable t) {
                            subscriber.onError(new NetworkConnectionException(t.getCause()));
                        }
                    });


                } catch (Exception e) {
                    subscriber.onError(new NetworkConnectionException(e.getCause()));
                }
            }
        });
    }
    // endregion





}

