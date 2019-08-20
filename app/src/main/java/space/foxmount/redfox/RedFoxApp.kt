package space.foxmount.redfox

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

class RedFoxApp : Application() {

    companion object {

        lateinit var instance: RedFoxApp

        fun appContext(): Context = instance.applicationContext

        fun isNetworkAvailable(): Boolean {
            val cm = instance.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo?.isConnected ?: false
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}