package com.bfu.loophintsearchview

import android.util.Log

private const val TAG = "MyAndroidTest"

fun logD(msg: () -> String) {
    val threadName = Thread.currentThread().name
    Log.d(TAG, "[$threadName] ${msg()}")
}

fun logE(msg: () -> String) {
    val threadName = Thread.currentThread().name
    Log.d(TAG, "[$threadName] ${msg()}")
}

fun logW(msg: () -> String) {
    val threadName = Thread.currentThread().name
    Log.d(TAG, "[$threadName] ${msg()}")
}

fun logI(msg: () -> String) {
    val threadName = Thread.currentThread().name
    Log.d(TAG, "[$threadName] ${msg()}")
}