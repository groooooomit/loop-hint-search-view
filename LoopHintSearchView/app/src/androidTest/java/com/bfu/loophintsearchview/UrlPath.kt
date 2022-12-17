package com.bfu.loophintsearchview

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UrlPath {
    @Test
    fun testUri() {
        val url = "http://www.baidu.com/a/bb/cc123?a=1&b=2"
        val pathTail = Uri.parse(url).lastPathSegment
        println("pathTail: $pathTail")
    }
}