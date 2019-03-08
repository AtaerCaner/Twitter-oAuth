package com.sahibinden.ataercanercelik.challenge.ui.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.sahibinden.ataercanercelik.challenge.R
import com.sahibinden.ataercanercelik.challenge.`interface`.TweetClickListener
import com.sahibinden.ataercanercelik.challenge.enum.ListFillAction
import com.sahibinden.ataercanercelik.challenge.ui.base.BaseActivity
import com.sahibinden.ataercanercelik.challenge.ui.search.adapter.TweetListRecyclerViewAdapter
import com.sahibinden.ataercanercelik.challenge.ui.search.detail.TweetDetailActivity
import com.sahibinden.ataercanercelik.challenge.ui.search.detail.TweetDetailFragment
import com.sahibinden.ataercanercelik.challenge.util.TwitterUtil
import com.sahibinden.ataercanercelik.challenge.util.TwitterUtil.getHeader
import com.domain.model.tweet.Tweet
import com.domain.model.tweet.TweetResponse
import com.domain.presenters.SearchPresenter
import com.domain.views.SearchTweetView
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_tweet_list.*
import kotlinx.android.synthetic.main.tweet_list.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class TweetListActivity : BaseActivity(), SearchTweetView, TweetClickListener {


    @Inject
    lateinit var searchPresenter: SearchPresenter

    private var twoPane: Boolean = false
    private val TWEET_DETAIL = 1234
    private val ERROR_TEXT = 0
    private val RECYCLER_VIEW = 1
    private val PROGRESS_VIEW = 2
    private val layoutManager by lazy { LinearLayoutManager(this) }
    private lateinit var tweetAdapter: TweetListRecyclerViewAdapter
    private var lastFillAction = ListFillAction.FIRST_LOAD
    private lateinit var lastSearchedText: String
    var visibleItemCount = 0
    var firstVisibleItem = 0
    var totalItemCount = 0
    var previousTotalItemCount = 0
    var loading = true
    var twoPaneFragment: Fragment? = null


    override fun init() {
        super.init()
        applicationComponent.inject(this)
        searchPresenter.attachGetTweetListView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_list)
        init()

        setSupportActionBar(toolbar)

        tweetAdapter = TweetListRecyclerViewAdapter(this,this)
        tweetListRecyclerView.layoutManager = layoutManager
        tweetListRecyclerView.adapter = tweetAdapter


        if (tweet_detail_container != null)
            twoPane = true

        RxSearchView.queryTextChangeEvents(searchTweetView)
                .debounce(750, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val queryString = TwitterUtil.convertToEng(it.queryText().toString())
                    if (queryString.trim().length > 2) {
                        resetPagination()
                        showProgress()
                        lastSearchedText = queryString
                        lastFillAction = ListFillAction.FIRST_LOAD
                        searchPresenter.search(getHeader(this, queryString), queryString)
                    } else {
                        showDummyText(getString(R.string.search_text))
                    }
                }



        tweetListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(v: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(v, dx, dy)
                if (dy > 10) {
                    closeKeyboard()
                }

                visibleItemCount = tweetListRecyclerView.childCount

                firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                totalItemCount = tweetAdapter.getDataItemCount()

                if (loading && totalItemCount > previousTotalItemCount) {
                    loading = false
                    previousTotalItemCount = totalItemCount
                }


                val visibleThreshold = 6
                if (totalItemCount > 2 && !loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                    loading = true
                    lastFillAction = ListFillAction.PAGINATION
                    val tweetId = tweetAdapter.getLastItemId()
                    searchPresenter.search(getHeader(this@TweetListActivity, lastSearchedText, tweetId), lastSearchedText, tweetId)
                }


            }
        })

        btnReload.setOnClickListener {
            tweetListRecyclerView.scrollToPosition(0)
            btnReload.visibility = View.GONE
        }

    }


    override fun onSearchResponse(response: TweetResponse) {

        if (response.statuses != null && response.statuses.size > 0) {

            if (lastFillAction == ListFillAction.RELOAD) {
                if (tweetAdapter.getFirstItemId() != response.statuses.first().id) {
                    showToast("New tweets arrived", this)
                    btnReload.visibility = View.VISIBLE
                    tweetAdapter.addToBegin(response.statuses)
                }
            } else {
                if (lastFillAction == ListFillAction.PAGINATION)
                    response.statuses.removeAt(0)

                btnReload.visibility = View.GONE
                tweetAdapter.addToEnd(response.statuses)
            }

            showData()
        } else {
            showDummyText(getString(R.string.empty_text))
        }
    }

    override fun onError(throwable: Throwable?) {
        if (throwable != null)
            showToast(throwable.message, this)

        showDummyText(getString(R.string.empty_text))
    }

    override fun onTweetClicked(tweet: Tweet) {
        searchTweetView.clearFocus()
        if (twoPane) {
            lastFillAction = ListFillAction.RELOAD
            searchPresenter.search(getHeader(this, lastSearchedText), lastSearchedText)
            twoPaneFragment = TweetDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(TweetDetailFragment.ARG_ITEM_ID, tweet)
                }
            }
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.tweet_detail_container, twoPaneFragment)
                    .commit()

        } else {
            val intent = Intent(this, TweetDetailActivity::class.java).apply {
                putExtra(TweetDetailFragment.ARG_ITEM_ID, tweet)
            }
            startActivityForResult(intent, TWEET_DETAIL)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TWEET_DETAIL && resultCode == Activity.RESULT_OK) {
            lastFillAction = ListFillAction.RELOAD
            searchPresenter.search(getHeader(this, lastSearchedText), lastSearchedText)
        }
    }


    private fun resetPagination() {
        if (twoPane) {
            if (twoPaneFragment != null)
                supportFragmentManager.beginTransaction().remove(twoPaneFragment).commit()
        }

        visibleItemCount = 0
        firstVisibleItem = 0
        totalItemCount = 0
        previousTotalItemCount = 0
        tweetListRecyclerView.scrollToPosition(0)
        tweetAdapter.clearList()
        lastSearchedText = ""
        loading = false
    }


    fun closeKeyboard() {
        searchTweetView.clearFocus()
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    private fun showData() {
        viewFlipper.displayedChild = RECYCLER_VIEW
    }

    private fun showDummyText(content: String) {
        txtDummy.text = content
        viewFlipper.displayedChild = ERROR_TEXT
    }

    private fun showProgress() {
        viewFlipper.displayedChild = PROGRESS_VIEW
    }


}
