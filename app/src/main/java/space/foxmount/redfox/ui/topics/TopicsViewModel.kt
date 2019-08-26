package space.foxmount.redfox.ui.topics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import space.foxmount.redfox.R
import space.foxmount.redfox.RedFoxApp
import space.foxmount.redfox.data.api.*
import space.foxmount.redfox.data.db.Topic
import space.foxmount.redfox.data.repository.RedditRepository
import space.foxmount.redfox.util.SingleLiveEvent
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TopicsViewModel(app: Application) : AndroidViewModel(app) {

    private val REDDIT_URL = "https://www.reddit.com"
    private val TOPICS_COUNT = 5

    private var lastShowedTopic: String? = null

    val parentJob = Job()
    val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    val scope = CoroutineScope(coroutineContext)


    var errorMessage = SingleLiveEvent<String>()
    var success = SingleLiveEvent<Boolean>()
    var isLoading = MutableLiveData<Boolean>()

    val topicsList: LiveData<List<Topic>>
    val topicLink = SingleLiveEvent<String>()

    @Inject
    lateinit var repo: RedditRepository

    init {

        (app as RedFoxApp).component.inject(this)

        requestData()
        topicsList = repo.getTopics()
    }

    fun refreshTopics(newData: Boolean = false) {
        if (newData) lastShowedTopic = null
        requestData()
    }

    fun requestData() {
        isLoading.postValue(true)

        isLoading.postValue(true)
        scope.launch {

            val resp = repo.api.getTopics(TOPICS_COUNT.toString(), lastShowedTopic)
            when (resp) {
                is Result.Success -> {
                    val response = resp.data as RedditResponse
                    repo.save(response)
                    success.postValue(true)
                    if (!response.data.children.isEmpty())
                        lastShowedTopic = response.data.children.last().data.name
                }
                is Result.Error -> {
                    success.postValue(false)
                    if (resp.errorCode == ERROR_TIMEOUT)
                        resp.errMsg =
                            getApplication<RedFoxApp>().getString(R.string.timeout_exception)
                    if (resp.errorCode == ERROR_NO_INTERNET)
                        resp.errMsg =
                            getApplication<RedFoxApp>().getString(R.string.no_internet_connection)
                    errorMessage.postValue(resp.errMsg)
                }
                is Result.ErrorTyped -> {
                    errorMessage.postValue((resp.errData as ErrorModel?)?.errorMessage)
                    success.postValue(false)

                }
            }
            isLoading.postValue(false)
        }
    }

    fun onTopicClicked(topic: Topic) {
        topicLink.postValue(REDDIT_URL + topic.fullLink)
    }

}
