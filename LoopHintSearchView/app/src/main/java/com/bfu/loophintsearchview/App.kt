package com.bfu.loophintsearchview

import android.app.Application

class App : Application() {

    init {
        instance = this
    }

    companion object {
        var instance: App? = null
    }
}