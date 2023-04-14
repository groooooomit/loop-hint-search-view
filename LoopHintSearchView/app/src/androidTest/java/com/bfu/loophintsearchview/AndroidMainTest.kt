package com.bfu.loophintsearchview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.coroutines.resume

@RunWith(AndroidJUnit4::class)
class AndroidMainTest {

    private val flag = MutableLiveData(false)

    private suspend fun awaitFlagUntilTrue() = suspendCancellableCoroutine { cont ->
        val observer = object : Observer<Boolean> {
            override fun onChanged(value: Boolean) {
                if (value) {
                    flag.removeObserver(this)
                    // flag now is True
                    cont.resume(Unit)
                }
            }
        }
        cont.invokeOnCancellation {
            flag.removeObserver(observer)
        }
        flag.observeForever(observer)
    }

    @Test
    fun testAwaitFlag(): Unit = runBlocking(Dispatchers.Main.immediate) {
        logD { "test begin, flag.value = ${flag.value}" }
        val c1 = launch {
            delay(2000)
            flag.value = true
        }
        val c2 = launch {
            awaitFlagUntilTrue()
            logD { "c2, flag.value = ${flag.value}" }
        }
        joinAll(c1, c2)
    }

}