package com.bfu.loophintsearchview.util

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

fun <T> Flow<T>.latest(): Flow<T> = object : Flow<T> {
    override suspend fun collect(collector: FlowCollector<T>) {
        var preJob: Job? = null
        coroutineScope {
            this@latest.collect {
                preJob?.cancelAndJoin()
                preJob = launch(start = CoroutineStart.UNDISPATCHED) {
                    collector.emit(it)
                }
            }
        }
    }
}

fun <T> Flow<List<T>>.shrink(): Flow<List<T>> = object : Flow<List<T>> {
    override suspend fun collect(collector: FlowCollector<List<T>>) {
        var previews: List<T> = emptyList()
        coroutineScope {
            this@shrink.collect {
                val result = if (previews.isEmpty()) it else it.toMutableList().apply {
                    removeAll(previews)
                }
                previews = result
                collector.emit(result)
            }
        }
    }
}

fun <T> Flow<T>.onMyEach(action: (T) -> Unit): Flow<T> {
    return MyOnEachFLow(this, action)
}

fun <T, R> Flow<T>.myMap(action: (T) -> R): Flow<R> {
    return MyMapFLow(this, action)
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