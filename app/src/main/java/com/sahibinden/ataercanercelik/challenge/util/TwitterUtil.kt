package com.sahibinden.ataercanercelik.challenge.util

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import com.sahibinden.ataercanercelik.challenge.R
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


object TwitterUtil {

    @Throws(UnsupportedEncodingException::class)
    fun encode(value: String): String {
        return URLEncoder.encode(value, "UTF-8")
    }

    fun generateSignature(signatueBaseStr: String, oAuthConsumerSecret: String): String {
        var byteHMAC: ByteArray? = null
        try {
            val mac = Mac.getInstance("HmacSHA1")
            val signingKey = encode(oAuthConsumerSecret) + '&'
            val spec = SecretKeySpec(signingKey.toByteArray(), "HmacSHA1")
            mac.init(spec)
            byteHMAC = mac.doFinal(signatueBaseStr.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(byteHMAC).removeSuffix("\n")
        } else {
            android.util.Base64.encodeToString(byteHMAC, android.util.Base64.DEFAULT).removeSuffix("\n")
        }


    }


    fun getHeader(context: Context, query: String): String {
        return getHeader(context, query, null)
    }

    fun getHeader(context: Context, query: String, maxId: Long?): String {
        val consumerSecret = context.resources.getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)
        val oauth_nonce = UUID.randomUUID().toString()
        val oauth_signature_method = "HMAC-SHA1"
        val oauth_timestamp = "${System.currentTimeMillis() / 1000L}"
        val oauth_consumer_key = context.resources.getString(R.string.com_twitter_sdk_android_CONSUMER_KEY)

        var maxIdQuery = ""

        if (maxId != null)
            maxIdQuery = "max_id=$maxId&"

        val queryString = "$maxIdQuery" +
                "oauth_consumer_key=$oauth_consumer_key&" +
                "oauth_nonce=$oauth_nonce&" +
                "oauth_signature_method=$oauth_signature_method&" +
                "oauth_timestamp=$oauth_timestamp&" +
                "oauth_version=1.0&" +
                "q=${query.replace(" ", "%20")}"


        val method = "GET"
        val url = "https://api.twitter.com/1.1/search/tweets.json"
        val baseString = "$method&${encode(url)}&${encode(queryString)}"


        val oauth_signature = generateSignature(baseString, consumerSecret)
        val preHeader =
                "oauth_consumer_key=\"$oauth_consumer_key\"," +
                        "oauth_signature_method=\"$oauth_signature_method\"," +
                        "oauth_timestamp=\"$oauth_timestamp\"," +
                        "oauth_nonce=\"$oauth_nonce\"," +
                        "oauth_version=\"1.0\"," +
                        "oauth_signature=\"${encode(oauth_signature)}\""


        return "OAuth $preHeader"
    }


    fun convertDate(dtString: String): String? {
        var dt: Date? = null
        var result: String = ""
        try {
            val df = SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
            df.timeZone = TimeZone.getTimeZone("UTC")

            dt = df.parse(dtString)
        } catch (e: Exception) {
        }

        if (dt != null) {
            val dfs = DateFormatSymbols()
            val cal = Calendar.getInstance()
            cal.time = dt

            val year = cal.get(Calendar.YEAR)
            val month = dfs.months[cal.get(Calendar.MONTH)]
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val min = if (cal.get(Calendar.MINUTE) < 10) "0${cal.get(Calendar.MINUTE)}" else "${cal.get(Calendar.MINUTE)}"
            result = "$hour:$min - $day $month $year"
        } else {
            val splited = dtString.split(" ".toRegex())

            try {
                result = "${splited[2]} ${splited[1]} ${splited[splited.size - 1]}"
            } catch (e: Exception) {
            }
        }



        return result
    }

    fun convertToEng(word: String): String {
        val turkishChars = listOf('ı', 'ü', 'ö', 'ş', 'ç', 'ğ')
        val englishChars = listOf('i', 'u', 'o', 's', 'c', 'g')
        var resultWord = word.toLowerCase(Locale("US"))

        for (i in 0 until turkishChars.size) {
            resultWord = resultWord.replace(turkishChars[i], englishChars[i])
        }

        return resultWord
    }


    fun fromHtml(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        else
            Html.fromHtml(html)
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

}
