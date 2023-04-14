package com.bfu.loophintsearchview

import com.bfu.loophintsearchview.util.latest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MainTest {

    @Test
    fun `test collectAtLatest`(): Unit = runBlocking(Dispatchers.Default) {
        val aFlow = flow {
            emit(1)
            emit(2)
            emit(3)
        }.latest()

        log { "collect start!" }
        aFlow
            .collect {
                log { "process $it begin" }
                delay(1000)
                log { "process $it end" }
            }
        log { "collect done!" }
    }

}