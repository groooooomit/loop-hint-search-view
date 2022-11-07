package com.bfu.loophintsearchview

import android.content.ContextWrapper

/** 全局 ApplicationContext. */
object AppContext : ContextWrapper(
    App.instance ?: error("'ContextHolder.sContext' NOT initialize!")
)

val Int.dp: Float
    get() = (this * AppContext.resources.displayMetrics.density)
