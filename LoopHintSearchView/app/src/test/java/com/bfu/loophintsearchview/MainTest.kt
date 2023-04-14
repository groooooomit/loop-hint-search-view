package com.bfu.loophintsearchview

import com.bfu.loophintsearchview.util.latest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MainTest {

    @Test
    fun `test collectAtLatest`() {
        runBlocking(Dispatchers.Default) {

            launch {
                log { "test 111" }
                coroutineScope {
                    log { "test 222" }
                    launch {
                        log { "test 333" }
                        delay(1000)
                        log { "test 444" }
                    }
                    log { "test 555" }
                }
                log { "test 666" }
            }.join()

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

            flow {
                emit(1)
                emit(2)
                emit(3)
            }.onEach {

            }.flowOn(Dispatchers.IO).collect(object : FlowCollector<Int> {
                override suspend fun emit(value: Int) {
                    println(value)
                }
            })
        }
    }

}