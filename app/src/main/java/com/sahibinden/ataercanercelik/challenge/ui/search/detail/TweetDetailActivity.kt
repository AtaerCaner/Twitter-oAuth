package com.sahibinden.ataercanercelik.challenge.ui.search.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.sahibinden.ataercanercelik.challenge.R
import kotlinx.android.synthetic.main.activity_tweet_detail.*



class TweetDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_detail)
        setSupportActionBar(detail_toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragment = TweetDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(TweetDetailFragment.ARG_ITEM_ID,
                            intent.getSerializableExtra(TweetDetailFragment.ARG_ITEM_ID))
                }
            }
//            addSourceSearchView.clearFocus()

            supportFragmentManager.beginTransaction()
                    .add(R.id.tweet_detail_container, fragment)
                    .commit()
        }
    }

    override fun onBackPressed() {
        finishActivity()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    finishActivity()
                    true
                }
                else -> super.onOptionsItemSelected(item)

            }

    private fun finishActivity() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
