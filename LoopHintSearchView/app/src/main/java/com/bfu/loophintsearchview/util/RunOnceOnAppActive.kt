package com.bfu.loophintsearchview.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job

object RunOnceOnAppActiveHelper {

    var currentResumedActivity: FragmentActivity? = null

    fun runOnceOnResumed(action: FragmentActivity.() -> Unit): Job = applicationScope
        .launchWhenResumed {
            currentResumedActivity?.action()
        }


    fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {
                currentResumedActivity = activity as? FragmentActivity
            }

            override fun onActivityPaused(activity: Activity) {
                currentResumedActivity = null
            }

            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}

        })
    }
}

val applicationScope: LifecycleCoroutineScope
    get() = ProcessLifecycleOwner.get().lifecycleScope