package com.sahibinden.ataercanercelik.challenge.`interface`

import com.domain.model.tweet.Tweet

interface TweetClickListener {
    fun onTweetClicked(tweet: Tweet)
}