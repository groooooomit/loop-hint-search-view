package com.bfu.loophintsearchview.base

import android.app.Application

class App : Application() {

    init {
        instance = this
    }

    companion object {
        var instance: App? = null

        /** 文字动画执行时长. */
        const val ANIM_DURATION = 1000L

        /** 文字展示时长. */
        const val ITEM_SHOWING_DURATION = 1000L
    }
}