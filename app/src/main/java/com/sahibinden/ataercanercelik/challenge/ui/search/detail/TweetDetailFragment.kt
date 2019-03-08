package com.sahibinden.ataercanercelik.challenge.ui.search.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.sahibinden.ataercanercelik.challenge.R
import com.sahibinden.ataercanercelik.challenge.util.TwitterUtil
import com.domain.model.tweet.Tweet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tweet_detail.view.*


class TweetDetailFragment : Fragment() {

    private var tweet: Tweet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                tweet = it.getSerializable(ARG_ITEM_ID) as Tweet
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tweet_detail, container, false)

        tweet?.let {
            rootView.txtDate.text = TwitterUtil.convertDate(it.createdAt)
            rootView.txtName.text = it.user.name
            rootView.txtUserName.text = "@${it.user.screenName}"
            rootView.txtRtCount.text = "${it.retweetCount}"
            rootView.txtFavCount.text = "${it.favoriteCount}"

            rootView.txtContent.text

            var mentionedText = it.text

            for (mentions in it.entities.userMentions)
                mentionedText = mentionedText.replace("@${mentions.screenName}\\b".toRegex(), "<font color=#2e82b3>@${mentions.screenName}</font>")

            for (hashtag in it.entities.hashtags)
                mentionedText = mentionedText.replace("#${hashtag.text}\\b".toRegex(), "<font color=#2e82b3>#${hashtag.text}</font>")


            rootView.txtContent.text = TwitterUtil.fromHtml(mentionedText)


            Picasso.with(context).load(it.user.profileImageUrl).fit().centerInside().into(rootView.imgProfile)

            if (it.extendedEntities != null && it.extendedEntities.media != null) {
                for (i in 0 until it.extendedEntities.media.size) {
                    val media = it.extendedEntities.media[i]
                    var toWhichView: ImageView? = null
                    val makeViewLarger = it.extendedEntities.media.size == 1
                    when (i) {
                        0 -> {
                            if (makeViewLarger) {
                                rootView.img0.layoutParams.height = TwitterUtil.pxToDp(media.sizes.large.h)
                                rootView.img0.requestLayout()
                            }
                            rootView.imgContainer0.visibility = View.VISIBLE
                            rootView.img0.visibility = View.VISIBLE
                            toWhichView = rootView.img0
                        }
                        1 -> {
                            rootView.imgContainer1.visibility = View.VISIBLE
                            rootView.img1.visibility = View.VISIBLE
                            toWhichView = rootView.img1
                        }
                        2 -> {
                            rootView.img2.visibility = View.VISIBLE
                            toWhichView = rootView.img2
                        }
                        3 -> {
                            rootView.img3.visibility = View.VISIBLE
                            toWhichView = rootView.img3
                        }
                    }

                    if (toWhichView != null)
                        Picasso.with(context).load(media.mediaUrl).into(toWhichView)
                }
            }
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}
