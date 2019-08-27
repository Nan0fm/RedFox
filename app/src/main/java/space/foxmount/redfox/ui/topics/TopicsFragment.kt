package space.foxmount.redfox.ui.topics

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.topics_fragment.*
import space.foxmount.redfox.R
import space.foxmount.redfox.data.db.Topic
import space.foxmount.redfox.ui.WebActivity
import space.foxmount.redfox.ui.base.BaseFragment
import space.foxmount.redfox.ui.helpers.CustomTabHelper
import space.foxmount.redfox.ui.topics.adapter.TopicsAdapter

class TopicsFragment : BaseFragment() {

    lateinit var adapter: TopicsAdapter

    private var customTabHelper: CustomTabHelper = CustomTabHelper()

    private lateinit var viewModel: TopicsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.topics_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TopicsViewModel::class.java)

        viewModel.topicLink observe { runChromeTabs(it) }

        viewModel.topicsList observe { showTopics(it) }

        viewModel.isLoading observe { setRefreshing(it) }

        viewModel.errorMessage observe { showError(it) }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TopicsAdapter { topic ->
            run {
                viewModel.onTopicClicked(topic)
            }
        }

        topicsRv.adapter = adapter

        topicsRv.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int, dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                val llManager =
                    topicsRv.layoutManager as LinearLayoutManager
                val visibleItemCount = llManager.childCount
                val totalItemCount = adapter.itemCount
                val firstVisibleItemPosition = llManager.findFirstVisibleItemPosition()


                if (!refreshTopics.isRefreshing) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        viewModel.refreshTopics()
                    }
                }
            }
        })

        refreshTopics.setOnRefreshListener { viewModel.refreshTopics(true) }
    }


    fun showTopics(topics: List<Topic>) {
        if (topics.isNotEmpty()) {
            rlEmpty.isVisible = false
            refreshTopics.isVisible = true
            adapter.addTopics(topics)
            adapter.notifyDataSetChanged()
        }
    }

    fun runChromeTabs(url: String) {

        val builder = CustomTabsIntent.Builder()

        builder.setToolbarColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        builder.addDefaultShareMenuItem()

        val anotherCustomTab = CustomTabsIntent.Builder().build()

        val intent = anotherCustomTab.intent
        intent.data = Uri.parse(url)

        builder.setShowTitle(true)
        builder.setStartAnimations(context!!, android.R.anim.fade_in, android.R.anim.fade_out)
        builder.setExitAnimations(context!!, android.R.anim.fade_in, android.R.anim.fade_out)

        val customTabsIntent = builder.build()

        val packageName = customTabHelper.getPackageNameToUse(context!!, url)

        if (packageName == null) {
            val intentOpenUri = Intent(context!!, WebActivity::class.java)
            intentOpenUri.putExtra(WebActivity.EXTRA_URL, Uri.parse(url).toString())
            startActivity(intentOpenUri)
        } else {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(context!!, Uri.parse(url))
        }

    }

    fun setRefreshing(isRefr: Boolean) {
        refreshTopics.isRefreshing = isRefr
    }

    fun showError(msg: String) {
        Snackbar.make(refreshTopics, msg, Snackbar.LENGTH_LONG).show()
    }


}
