package space.foxmount.redfox.ui.topics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import space.foxmount.redfox.R
import space.foxmount.redfox.data.db.Topic
import space.foxmount.redfox.ui.topics.adapter.view_holders.ImageTopicVh
import space.foxmount.redfox.ui.topics.adapter.view_holders.LinkTopicVh
import space.foxmount.redfox.ui.topics.adapter.view_holders.VideoTopicVh

class TopicsAdapter(
    var topics: MutableList<Topic> = mutableListOf(),
    val itemClick: (Topic) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val CARD_TYPE_IMAGE = 1
    private val CARD_TYPE_LINK = 2
    private val CARD_TYPE_H_VIDEO = 3
    private val CARD_TYPE_R_VIDEO = 4
    private val CARD_TYPE_SELF = 5

    enum class TopicTypes(val type: String) {
        IMAGE("image")
        ,
        LINK("link")
        ,
        HOSTED_VIDEO("hosted:video")
        ,
        RICH_VIDEO("rich:video")
        ,
        SELF("self")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var layoutId = R.layout.card_topic_image
        when (viewType) {
            CARD_TYPE_IMAGE -> {
                layoutId = R.layout.card_topic_image
                return ImageTopicVh(inflater.inflate(layoutId, parent, false), itemClick)
            }
            CARD_TYPE_LINK, CARD_TYPE_SELF -> {
                layoutId = R.layout.card_topic_link
                return LinkTopicVh(inflater.inflate(layoutId, parent, false), itemClick)
            }
            CARD_TYPE_H_VIDEO, CARD_TYPE_R_VIDEO -> {
                layoutId = R.layout.card_topic_video
                return VideoTopicVh(inflater.inflate(layoutId, parent, false), itemClick)
            }

        }
        return ImageTopicVh(inflater.inflate(layoutId, parent, false), itemClick)
    }


    override fun getItemViewType(position: Int): Int {

        if (topics[position].topicType != null) {
            when (topics[position].topicType) {
                TopicTypes.IMAGE.type -> return CARD_TYPE_IMAGE
                TopicTypes.HOSTED_VIDEO.type -> return CARD_TYPE_LINK
                TopicTypes.LINK.type -> return CARD_TYPE_H_VIDEO
                TopicTypes.RICH_VIDEO.type -> return CARD_TYPE_R_VIDEO
                TopicTypes.SELF.type -> return CARD_TYPE_SELF
            }
        } else {
            return CARD_TYPE_SELF
        }

        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val t = topics[position]
        when (holder) {
            is ImageTopicVh -> {
                holder.parseTopic(t)
            }
            is LinkTopicVh -> {
                holder.parseTopic(t)
            }
            is VideoTopicVh -> {
                holder.parseTopic(t)
            }
        }

    }

    override fun getItemCount(): Int {
        return topics.size
    }

    fun addTopics(newTopics: List<Topic>) {
        topics.addAll(newTopics.filter { !topics.map { t -> t.name }.contains(it.name) })
    }

}