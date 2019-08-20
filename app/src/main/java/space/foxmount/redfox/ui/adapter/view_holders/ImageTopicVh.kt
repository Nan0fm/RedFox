package space.foxmount.redfox.ui.adapter.view_holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import space.foxmount.redfox.R
import space.foxmount.redfox.domain.Utils
import space.foxmount.redfox.domain.model.Topic

class ImageTopicVh(
    view: View
    , val topicClick: (Topic) -> Unit
) : RecyclerView.ViewHolder(view) {

    val subredditView: TextView = view.findViewById(R.id.subredditTextView)
    val authorView: TextView = view.findViewById(R.id.authorView)
    val rate: TextView = view.findViewById(R.id.rateView)
    val commentsView: TextView = view.findViewById(R.id.commentsView)
    val titleView: TextView = view.findViewById(R.id.titleTextView)

    var titleImage: ImageView = view.findViewById(R.id.postImageView)
    var topicImage: ImageView = view.findViewById(R.id.mainImageView)


    fun parseTopic(topic: Topic) {
        with(topic) {
            subredditView.text = "r/${subreddit}"
            val postedDate = Utils().getTimeAgo(postDate)
            authorView.text = "Posted by u/${authorName} ${postedDate}"
            titleView.text = title
            rate.text = rating.toString()
            commentsView.text = commentsCount.toString()
            Glide
                .with(titleImage)
                .load(thumbUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_av_timer_24dp)
                .into(titleImage)
            Glide
                .with(topicImage)
                .load(urlLink)
                .centerCrop()
                .placeholder(R.drawable.ic_av_timer_24dp)
                .into(topicImage)
            itemView.setOnClickListener { v ->
                Toast.makeText(v.context, authorName, android.widget.Toast.LENGTH_SHORT).show()
            }
            itemView.setOnClickListener { topicClick(topic) }


        }
    }

}