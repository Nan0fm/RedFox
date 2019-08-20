package space.foxmount.redfox.domain

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