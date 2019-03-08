package com.sahibinden.ataercanercelik.data.net;

import com.domain.model.tweet.TweetResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Service {


    @GET("search/tweets.json")
    Call<TweetResponse> search(@Header("Authorization") String Authorization, @Query("q") String q);

    @GET("search/tweets.json")
    Call<TweetResponse> search(@Header("Authorization") String Authorization, @Query("q") String q, @Query("max_id") String maxId);

}
