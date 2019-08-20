package space.foxmount.redfox.ui.helpers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.customtabs.CustomTabsService
import android.text.TextUtils

class CustomTabHelper {

    companion object {
        var packageNameToUse: String? = null
        val STABLE_PACKAGE = "com.android.chrome"
        val BETA_PACKAGE = "com.chrome.beta"
        val DEV_PACKAGE = "com.chrome.dev"
        val LOCAL_PACKAGE = "com.google.android.apps.chrome"
    }

    fun getPackageNameToUse(context: Context, url: String): String? {

        packageNameToUse?.let {
            return it
        }

        val pm = context.packageManager

        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0)
        var defaultViewHandlerPackageName: String? = null

        defaultViewHandlerInfo?.let {
            defaultViewHandlerPackageName = it.activityInfo.packageName
        }

        val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
        val packagesSupportingCustomTabs = ArrayList<String>()
        for (info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)

            pm.resolveService(serviceIntent, 0)?.let {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName)
            }
        }

        when {
            packagesSupportingCustomTabs.isEmpty() -> packageNameToUse = null
            packagesSupportingCustomTabs.size == 1 -> packageNameToUse =
                packagesSupportingCustomTabs.get(0)
            !TextUtils.isEmpty(defaultViewHandlerPackageName)
                    && !hasSpecializedHandlerIntents(context, activityIntent)
                    && packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName) ->
                packageNameToUse = defaultViewHandlerPackageName
            packagesSupportingCustomTabs.contains(STABLE_PACKAGE) -> packageNameToUse =
                STABLE_PACKAGE
            packagesSupportingCustomTabs.contains(BETA_PACKAGE) -> packageNameToUse = BETA_PACKAGE
            packagesSupportingCustomTabs.contains(DEV_PACKAGE) -> packageNameToUse = DEV_PACKAGE
            packagesSupportingCustomTabs.contains(LOCAL_PACKAGE) -> packageNameToUse = LOCAL_PACKAGE
        }
        return packageNameToUse
    }

    private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
        try {
            val pm = context.packageManager
            val handlers = pm.queryIntentActivities(
                intent,
                PackageManager.GET_RESOLVED_FILTER
            )
            if (handlers == null || handlers.size == 0) {
                return false
            }
            for (resolveInfo in handlers) {
                val filter = resolveInfo.filter ?: continue
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue
                if (resolveInfo.activityInfo == null) continue
                return true
            }
        } catch (e: RuntimeException) {
        }
        return false
    }

}