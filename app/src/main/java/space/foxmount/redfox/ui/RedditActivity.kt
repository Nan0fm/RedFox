package space.foxmount.redfox.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.arellomobile.mvp.MvpAppCompatActivity
import kotlinx.android.synthetic.main.activity_reddit.*
import space.foxmount.redfox.R
import space.foxmount.redfox.data.db.DbRepository
import space.foxmount.redfox.domain.model.Topic
import space.foxmount.redfox.ui.adapter.TopicsAdapter
import space.foxmount.redfox.ui.helpers.CustomTabHelper


class RedditActivity : MvpAppCompatActivity(), IRedditView {


    //    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var redditPresenter: RedditPresenter
    lateinit var adapter: TopicsAdapter

    private var customTabHelper: CustomTabHelper = CustomTabHelper()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reddit)

        redditPresenter = RedditPresenter(DbRepository(this))
        redditPresenter.attachView(this)

        adapter = TopicsAdapter { topic ->
            run {
                redditPresenter.onTopicClicked(topic)

            }
        }

        topicsRv.adapter = adapter

        topicsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int, dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                val llManager = topicsRv.layoutManager as LinearLayoutManager
                val visibleItemCount = llManager.childCount
                val totalItemCount = adapter.itemCount
                val firstVisibleItemPosition = llManager.findFirstVisibleItemPosition()


                if (!refreshTopics.isRefreshing) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        redditPresenter.refreshTopics()
                    }
                }
            }
        })

        refreshTopics.setOnRefreshListener { redditPresenter.refreshTopics(true) }
    }


    override fun openTopic(topic: Topic) {
        Toast.makeText(this@RedditActivity, topic.authorName, LENGTH_SHORT).show()
    }

    override fun showTopics(topics: List<Topic>) {
        adapter.addTopics(topics)
        adapter.notifyDataSetChanged()
    }

    override fun runChromeTabs(url: String) {

        val builder = CustomTabsIntent.Builder()

        builder.setToolbarColor(ContextCompat.getColor(this@RedditActivity, R.color.colorPrimary))
        builder.addDefaultShareMenuItem()

        val anotherCustomTab = CustomTabsIntent.Builder().build()

        val intent = anotherCustomTab.intent
        intent.data = Uri.parse(url)

        builder.setShowTitle(true)
        builder.setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
        builder.setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)

        val customTabsIntent = builder.build()

        val packageName = customTabHelper.getPackageNameToUse(this, url)

        if (packageName == null) {
            val intentOpenUri = Intent(this, WebActivity::class.java)
            intentOpenUri.putExtra(WebActivity.EXTRA_URL, Uri.parse(url).toString())
            startActivity(intentOpenUri)
        } else {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(this, Uri.parse(url))
        }


    }

    override fun setRefreshing(isRefr: Boolean) {
        refreshTopics.isRefreshing = isRefr
    }

    override fun showError(msg: String) {
        Toast.makeText(this@RedditActivity, msg, LENGTH_SHORT).show()
    }

}
