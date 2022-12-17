package com.bfu.loophintsearchview

import kotlin.coroutines.suspendCoroutine

object SuspendTest2 {

    suspend fun main() {
        aaa()
        bbb()
        ccc()
    }

    suspend fun aaa() {
        ddd()
        eee()

//        suspendCoroutine<> {  }
    }

    suspend fun bbb() {
        fff()
        ggg()
    }

    suspend fun ccc() {

    }

    suspend fun ddd() {

    }

    suspend fun eee() {

    }

    suspend fun fff() {

    }

    suspend fun ggg() {

    }
}