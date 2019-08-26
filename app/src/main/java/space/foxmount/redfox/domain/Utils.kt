package space.foxmount.redfox.domain

import android.text.SpannableStringBuilder
import space.foxmount.redfox.data.db.Topic
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    fun stringToDate(date: String): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        var convertedDate = Date()
        try {
            convertedDate = dateFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return convertedDate
    }

    fun getPostStats(postEntity: Topic): SpannableStringBuilder {
        val statStringBuilder = SpannableStringBuilder()
        statStringBuilder.append(withSuffix(postEntity.rating) + " points" + "\n" +
                postEntity.commentsCount + " comments")
        return statStringBuilder
    }

    fun withSuffix(count: Int): String {
        return when {
            count < 10000 -> count.toString()
            count < 100000 -> (count / 1000).toString() + "k"
            else -> (count / 1000).toString()
        }
    }

    fun getTimeAgo(postedDate: String): String {
        val date = stringToDate(postedDate)
        val nowDate = Date()

        val diff = nowDate.time - date.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        if (days.toInt() > 0) return "${days} days ago"
        if (hours.toInt() > 0) return "${hours} hours ago"
        if (minutes.toInt() > 0) return "${minutes} minutes ago"
        if (seconds.toInt() > 0) return "${seconds} seconds ago"
        return "now"
    }

}