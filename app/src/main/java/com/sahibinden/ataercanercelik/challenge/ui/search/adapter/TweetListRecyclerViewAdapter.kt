package com.sahibinden.ataercanercelik.challenge.ui.search.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sahibinden.ataercanercelik.challenge.R
import com.sahibinden.ataercanercelik.challenge.`interface`.TweetClickListener
import com.sahibinden.ataercanercelik.challenge.ui.search.TweetListActivity
import com.sahibinden.ataercanercelik.challenge.util.TwitterUtil
import com.domain.model.tweet.Tweet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tweet_detail.view.*

class TweetListRecyclerViewAdapter(private val parentActivity: TweetListActivity,
                                   private val tweetClickListener: TweetClickListener) :
        RecyclerView.Adapter<TweetListRecyclerViewAdapter.ViewHolder>() {

    private var tweetList: MutableList<Tweet> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.tweet_list_content, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tweet = tweetList[position]

        var mentionedText = tweet.text

        for (mentions in tweet.entities.userMentions)
            mentionedText = mentionedText.replace("@${mentions.screenName}\\b".toRegex(), "<font color=#2e82b3>@${mentions.screenName}</font>")

        for (hashtag in tweet.entities.hashtags)
            mentionedText = mentionedText.replace("#${hashtag.text}\\b".toRegex(), "<font color=#2e82b3>#${hashtag.text}</font>")



        holder.txtContent.text = TwitterUtil.fromHtml(mentionedText)
        holder.txtName.text = tweet.user.name
        holder.txtUserName.text = "@${tweet.user.screenName}"
        holder.imgContainer0.visibility = View.GONE
        holder.imgContainer1.visibility = View.GONE
        holder.imgMedia0.visibility = View.GONE
        holder.imgMedia1.visibility = View.GONE
        holder.imgMedia2.visibility = View.GONE
        holder.imgMedia3.visibility = View.GONE

        Picasso.with(parentActivity).load(tweet.user.profileImageUrl).fit().centerInside().into(holder.imgProfile)

        if (tweet.extendedEntities != null && tweet.extendedEntities.media != null) {
            for (i in 0 until tweet.extendedEntities.media.size) {
                val media = tweet.extendedEntities.media[i]
                var toWhichView: ImageView? = null
                when (i) {
                    0 -> {
                        holder.imgContainer0.visibility = View.VISIBLE
                        holder.imgMedia0.visibility = View.VISIBLE
                        toWhichView = holder.imgMedia0
                    }
                    1 -> {
                        holder.imgContainer1.visibility = View.VISIBLE
                        holder.imgMedia1.visibility = View.VISIBLE
                        toWhichView = holder.imgMedia1
                    }
                    2 -> {
                        holder.imgMedia2.visibility = View.VISIBLE
                        toWhichView = holder.imgMedia2
                    }
                    3 -> {
                        holder.imgMedia3.visibility = View.VISIBLE
                        toWhichView = holder.imgMedia3
                    }
                }

                if (toWhichView != null)
                    Picasso.with(parentActivity).load(media.mediaUrl).into(toWhichView)
            }
        }

        holder.itemView.setOnClickListener {
            tweetClickListener.onTweetClicked(tweet)
        }
    }


    override fun getItemCount() = tweetList.size

    fun addToEnd(tweetList: List<Tweet>) {
        this.tweetList.addAll(tweetList)
        notifyDataSetChanged()
    }

    fun addToBegin(tweetList: List<Tweet>) {
        val listToAdd : MutableList<Tweet> = arrayListOf()

        for (i in 0 until tweetList.size) {
            if (tweetList[i] != this.tweetList[i]) {
                listToAdd.add(tweetList[i])
            }
        }

        for (i in listToAdd.indices.reversed())
            this.tweetList.add(0, listToAdd[i])

        notifyItemRangeInserted(0,tweetList.size)
    }

    fun clearList() {
        tweetList.clear()
        notifyDataSetChanged()
    }

    fun getDataItemCount(): Int = tweetList.size

    fun getItemList(): List<Tweet> = tweetList

    fun getLastItemId(): Long = tweetList.last().id

    fun getFirstItemId(): Long = tweetList.first().id

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.txtName
        val txtUserName: TextView = view.txtUserName
        val txtContent: TextView = view.txtContent
        val imgProfile: ImageView = view.imgProfile
        val imgMedia0: ImageView = view.img0
        val imgMedia1: ImageView = view.img1
        val imgMedia2: ImageView = view.img2
        val imgMedia3: ImageView = view.img3
        val imgContainer0: LinearLayout = view.imgContainer0
        val imgContainer1: LinearLayout = view.imgContainer1
    }
}