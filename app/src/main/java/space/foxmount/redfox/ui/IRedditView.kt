package space.foxmount.redfox.ui

import space.foxmount.redfox.data.db.Topic

interface IRedditView {

    fun showTopics(topics: List<Topic>)

    fun openTopic(topic: Topic)
    fun showError(localizedMessage: String)

    fun setRefreshing(isRefr: Boolean)
    fun runChromeTabs(url: String)

}