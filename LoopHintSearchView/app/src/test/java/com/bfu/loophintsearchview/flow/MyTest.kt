package com.bfu.loophintsearchview.flow

import com.bfu.loophintsearchview.util.myLatest
import com.bfu.loophintsearchview.util.onMyEach
import io.reactivex.Observable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyTest {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")
    private fun log(msg: () -> String) {
        val time = LocalDateTime.now().format(dateFormatter)
        val threadName = Thread.currentThread().name
        println("$time [$threadName] ${msg()}")
    }

    @Test
    fun `test collectAtLatest`() {
        Observable.fromIterable(listOf(100)).repeat()
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
            }.myLatest()

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

    @Test
    fun `test on my each`() {

//        listOf(1, 2, 3, 4)
//            .onEach {
//
//            }.map {
//
//            }
//
//        sequenceOf(
//            1, 2, 3, 4
//        ).onEach {
//
//        }.map {
//
//        }.toList()

//        channelFlow {
//            send(1)
//            send(2)
//            send(3)
//        }


        runBlocking {
//            val a = Channel<Int>()
//            a.send(1)
//            a.send(2)
//            a.send(3)
//            a.receiveAsFlow()
//
//            val b: SendChannel<Int> = GlobalScope.actor<Int> {
//                for (i in this) {
//                    println(i)
//                }
//            }
//            val s: ReceiveChannel<Int> = GlobalScope.produce<Int> {
//                send(1)
//                send(1)
//                send(1)
//            }
//            s.consumeEach {
//
//            }
//
//            val d = BroadcastChannel<Int>(10)
//            d.openSubscription().receiveAsFlow()
//
//            val e = GlobalScope.broadcast(capacity = 10) {
//                send(1)
//                send(2)
//                send(3)
//            }
// buffer() 0 防止内部组合出现非预期的情况

            flow {
                emit(1) // printLn(1)
                emit(2) // printLn(2)
                emit(3) // printLn(3)
//                (0..100).forEach {
//                    emit(it)
//                }
            }.mapLatest { }
                .onMyEach {
                    println("onEach $it")
                }.myLatest()
                .collectLatest {
                    println("process $it begin")
                    delay(100)
                    println("process $it end")
                }

            // 1>, 1!, 2>, 2!, 3>, 3!
            //
        }

    }
}