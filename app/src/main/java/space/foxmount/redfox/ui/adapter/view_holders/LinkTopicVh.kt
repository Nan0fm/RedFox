package space.foxmount.redfox.ui.adapter.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import space.foxmount.redfox.R
import space.foxmount.redfox.data.db.Topic
import space.foxmount.redfox.domain.Utils

class LinkTopicVh(
    view: View
    , val topicClick: (Topic) -> Unit
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    val subredditView: TextView = view.findViewById(R.id.subredditTextView)
    val authorView: TextView = view.findViewById(R.id.authorView)
    val rate: TextView = view.findViewById(R.id.rateView)
    val commentsView: TextView = view.findViewById(R.id.commentsView)
    val titleView: TextView = view.findViewById(R.id.titleTextView)
    val linkTextView: TextView = view.findViewById(R.id.linkTextView)
    val selfTextView: TextView = view.findViewById(R.id.selfTextView)

    var thumbImage: ImageView = view.findViewById(R.id.postImageView)


    fun parseTopic(topic: Topic) {
        with(topic) {
            subredditView.text = "r/${subreddit}"
            val postedDate = Utils().getTimeAgo(postDate)
            authorView.text = "Posted by u/${authorName} ${postedDate}"
            titleView.text = title
            rate.text = rating.toString()
            commentsView.text = commentsCount.toString()

            linkTextView.text = urlLink
            selfTextView.text = selfText

            Glide
                .with(thumbImage)
                .load(thumbUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_av_timer_24dp)
                .into(thumbImage)
            itemView.setOnClickListener { v ->
                Toast.makeText(v.context, authorName, Toast.LENGTH_SHORT).show()
            }
            itemView.setOnClickListener { topicClick(this) }


        }
    }

}