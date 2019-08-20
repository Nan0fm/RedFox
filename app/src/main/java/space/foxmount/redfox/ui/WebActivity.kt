package space.foxmount.redfox.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import space.foxmount.redfox.R

class WebActivity : AppCompatActivity() {

    companion object {
        val EXTRA_URL = "extra.url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
    }
}
