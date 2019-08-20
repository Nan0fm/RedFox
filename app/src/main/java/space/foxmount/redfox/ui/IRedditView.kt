package space.foxmount.redfox.ui

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import space.foxmount.redfox.domain.model.Topic

@StateStrategyType(AddToEndSingleStrategy::class)
interface IRedditView : MvpView {

    fun showTopics(topics: List<Topic>)

    fun openTopic(topic: Topic)
    fun showError(localizedMessage: String)

    fun setRefreshing(isRefr: Boolean)
    fun runChromeTabs(url: String)

}