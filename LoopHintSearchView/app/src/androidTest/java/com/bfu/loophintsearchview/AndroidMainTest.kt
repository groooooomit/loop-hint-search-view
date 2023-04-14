package com.bfu.loophintsearchview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.coroutines.resume

@RunWith(AndroidJUnit4::class)
class AndroidMainTest {

    ///////////////////////////////////////////////////////////////////////////
    // livedata
    ///////////////////////////////////////////////////////////////////////////

    private val flagLiveData = MutableLiveData(false)

    private suspend fun awaitFlagUntilTrueByLiveData() = suspendCancellableCoroutine { cont ->
        val observer = object : Observer<Boolean> {
            override fun onChanged(value: Boolean) {
                if (value) {
                    flagLiveData.removeObserver(this)
                    // flag now is True
                    cont.resume(Unit)
                }
            }
        }
        cont.invokeOnCancellation {
            flagLiveData.removeObserver(observer)
        }
        flagLiveData.observeForever(observer)
    }

    ///////////////////////////////////////////////////////////////////////////
    // flow
    ///////////////////////////////////////////////////////////////////////////

    private val flagFlow = MutableStateFlow(false)

    private suspend fun awaitFlagUntilTrueByFlow() {
        flagFlow.firstOrNull { it }
    }

    @Test
    fun testAwaitFlagUntilTrueByLiveData(): Unit = runBlocking(Dispatchers.Main.immediate) {
        logD { "testAwaitFlagUntilTrueByLiveData begin, flag.value = ${flagLiveData.value}" }
        val c1 = launch {
            delay(2000)
            flagLiveData.value = true
        }
        val c2 = launch {
            awaitFlagUntilTrueByLiveData()
            logD { "c2, flag.value = ${flagLiveData.value}" }
        }
        joinAll(c1, c2)
    }

    @Test
    fun testAwaitFlagUntilTrueByFlow(): Unit = runBlocking(Dispatchers.Main.immediate) {
        logD { "testAwaitFlagUntilTrueByFlow begin, flag.value = ${flagFlow.value}" }
        val c1 = launch {
            delay(2000)
            flagFlow.value = true
        }
        val c2 = launch {
            awaitFlagUntilTrueByFlow()
            logD { "c2, flag.value = ${flagFlow.value}" }
        }
        joinAll(c1, c2)
    }

}