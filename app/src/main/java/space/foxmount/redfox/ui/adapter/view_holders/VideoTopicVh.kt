package space.foxmount.redfox.ui.adapter.view_holders

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import space.foxmount.redfox.R
import space.foxmount.redfox.data.db.Topic
import space.foxmount.redfox.domain.Utils


class VideoTopicVh(
    view: View
    , val topicClick: (Topic) -> Unit
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    val subredditView: TextView = view.findViewById(R.id.subredditTextView)
    val authorView: TextView = view.findViewById(R.id.authorView)
    val rate: TextView = view.findViewById(R.id.rateView)
    val commentsView: TextView = view.findViewById(R.id.commentsView)
    val titleView: TextView = view.findViewById(R.id.titleTextView)


    var gifView: ImageView = view.findViewById(R.id.gifImageView)
    var video: VideoView = view.findViewById(R.id.videoView)


    fun parseTopic(topic: Topic) {
        with(topic) {

            subredditView.text = "r/${subreddit}"
            val postedDate = Utils().getTimeAgo(postDate)
            authorView.text = "Posted by u/${authorName} ${postedDate}"
            titleView.text = title
            rate.text = rating.toString()
            commentsView.text = commentsCount.toString()


            videoLink?.let {
                if (isGif) {
                    video.visibility = INVISIBLE
                    gifView.visibility = VISIBLE
                    Glide
                        .with(itemView)
                        .load(videoLink + ".gif")

                        .error(R.drawable.ic_av_timer_24dp)
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                        .into(gifView)
                } else {
                    video.visibility = VISIBLE
                    gifView.visibility = INVISIBLE
                    video.setVideoPath(videoLink)
                    video.start()

                }
            }

            itemView.setOnClickListener { v ->
                Toast.makeText(v.context, authorName, android.widget.Toast.LENGTH_SHORT).show()
            }
            itemView.setOnClickListener { topicClick(this) }


        }
    }

}