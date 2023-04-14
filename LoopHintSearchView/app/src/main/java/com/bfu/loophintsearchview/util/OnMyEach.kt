package com.bfu.loophintsearchview.util

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

fun <T> Flow<T>.onMyEach(action: (T) -> Unit): Flow<T> {
    return MyOnEachFLow(this, action)
}

fun <T, R> Flow<T>.myMap(action: (T) -> R): Flow<R> {
    return MyMapFLow(this, action)
}


fun <T> Flow<T>.myLatest(): Flow<T> {
    return MyLatestFlow(this)
}


class MyOnEachFLow<T>(
    private val originFlow: Flow<T>,
    private val action: (T) -> Unit,
) : Flow<T> {
    override suspend fun collect(collector: FlowCollector<T>) {
        originFlow.collect {
            action(it)
            collector.emit(it)
        }
    }

}

class MyMapFLow<T, R>(
    private val originFlow: Flow<T>,
    private val action: (T) -> R,
) : Flow<R> {
    override suspend fun collect(collector: FlowCollector<R>) {
        originFlow.collect {
            val result = action(it)
            collector.emit(result)
        }
    }

}

class MyLatestFlow<T>(
    private val originFLow: Flow<T>,
) : Flow<T> {
    override suspend fun collect(collector: FlowCollector<T>) {
        var preJob: Job? = null
        coroutineScope {
            println("inner collect start")
            originFLow.collect {
                preJob?.cancelAndJoin()
                preJob = launch(start = CoroutineStart.UNDISPATCHED) {
                    collector.emit(it)
                }
            }
            println("inner collect done 1")
        }
        println("inner collect done 2")
    }
}