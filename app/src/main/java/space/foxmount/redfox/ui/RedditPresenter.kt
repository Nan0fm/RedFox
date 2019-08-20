package space.foxmount.redfox.ui

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import space.foxmount.redfox.data.api.ApiRepository
import space.foxmount.redfox.data.api.RedditService
import space.foxmount.redfox.data.db.DbRepository
import space.foxmount.redfox.data.repository.RedditRepository
import space.foxmount.redfox.domain.model.Topic

@InjectViewState
class RedditPresenter(db: DbRepository) : MvpPresenter<IRedditView>() {

    private val REDDIT_URL = "https://www.reddit.com"
    private val TOPICS_COUNT = 5

    private var lastShowedTopic: String? = null

    val myCompositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    var repo: RedditRepository =
        RedditRepository(ApiRepository(RedditService.getInstance()!!.getApiData()), db)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        requestData()
    }

    fun refreshTopics(newData: Boolean = false) {
        if (newData) lastShowedTopic = null
        requestData()
    }

    fun requestData() {
        viewState.setRefreshing(true)
        myCompositeDisposable.add(
            repo.getAll(TOPICS_COUNT, lastShowedTopic)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(topicsList: List<Topic>) {
        viewState.setRefreshing(false)
        viewState.showTopics(topicsList)
        if (!topicsList.isEmpty())
            lastShowedTopic = topicsList.last().name
    }

    fun onTopicClicked(topic: Topic) {
        viewState.runChromeTabs(REDDIT_URL + topic.fullLink)
    }


    fun getAllPosts() {
        myCompositeDisposable.add(repo.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { handleResponse(it) },
                { handleError(it) }
            ))
    }

//    fun getPosts(page: Int) {
//        postRepo.query().where("page", page.toString())
//            .findAll()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { showPosts(it) },
//                { /* error handling */ }
//            )
//    }

    private fun handleError(it: Throwable?) {


    }

}