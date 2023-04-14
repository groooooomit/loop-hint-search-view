package com.bfu.loophintsearchview.preinstall

import io.reactivex.Completable
import kotlinx.coroutines.delay
import org.junit.Test
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.coroutines.*

class ThreadVsCoroutine {


    @Test
    fun `thread vs coroutines`() {
        val a = Runnable {
            Thread.sleep(3000)
            println("Hello world")
        }

        val thread = Thread(a)
        thread.start()

        //

        val b = suspend {
            delay(1000)
            println("hello world")
        }
        val coroutine = b.createCoroutine(object : Continuation<Unit> {
            override val context = EmptyCoroutineContext
            override fun resumeWith(result: Result<Unit>) {

            }
        })
        coroutine.resume(Unit)
        //
        val completable = Completable.complete()
            .delay(1000, TimeUnit.MILLISECONDS)
            .doOnComplete { println("hello world") }
            .doOnComplete {

            }
            .doOnError {

            }
        completable.subscribe()
        //
    }

    object HttpClient {

        @JvmField
        val c: List<Result<String>> = emptyList()

        @JvmStatic
        suspend fun request2(param: Int) {

        }

        @JvmField
        val a = suspend {
            "xxxxx"
        }

        @JvmField
        val a2 = {
            "xxxxx"
        }

        fun request(param: Int, callback: CommonCallback) {
            thread {
                Thread.sleep(3000)
                callback.onResult(Result.success("http response $param"))
            }
        }

        fun request2(param: Int, callback: CommonCallback) {
            val completion = object : CommonCallback {
                var label = 0
                override fun onResult(result: Result<Any?>) {
                    request2(param, this)
                }

            }
            when (completion.label) {
                0 -> {
//                    request3()
                }
            }
            return completion.onResult(Result.success(Unit))
        }

        fun request3(param: Int, callback: CommonCallback) {
            thread {
                Thread.sleep(3000)
                callback.onResult(Result.success("http response $param"))
            }
        }
    }

    object Dao {
        fun save(data: String, callback: CommonCallback) {
            thread {
                Thread.sleep(3000)
                callback.onResult(Result.success(data.hashCode()))
            }
        }
    }

    object DownloadManager {
        fun downloadFile(url: String, callback: CommonCallback) {
            thread {
                Thread.sleep(3000)
                callback.onResult(Result.success(File("xxx")))
            }
        }
    }

    interface CommonCallback {
        fun onResult(result: Result<Any?>)
    }

    @Test
    fun `coroutine test`() {
//        val disposable = Completable.complete()
//            .andThen(Single.create {
//                HttpClient.request(1, object : CommonCallback {
//                    override fun onSuccess(response: String) {
//                        it.onSuccess(response)
//                    }
//                    override fun onError(error: Exception) {
//                        it.onError(error)
//                    }
//                })
//            })
//            .flatMap { response ->
//                Single.create {
//                    Dao.save(response, object : CommonCallback {
//                        override fun onSuccess(id: Int) {
//                            it.onSuccess(id)
//                        }
//
//                        override fun onError(error: Exception) {
//                            it.onError(error)
//                        }
//                    })
//                }
//            }.flatMap { id ->
//                Single.create {
//                    DownloadManager.downloadFile("xxx$id", object : DownloadManager.Callback {
//                        override fun onSuccess(file: File) {
//                            it.onSuccess(file)
//                        }
//
//                        override fun onError(error: Exception) {
//                            it.onError(error)
//                        }
//                    })
//                }
//            }.subscribeBy(
//                onSuccess = { file ->
//                    println("file download success, ${file.path}")
//                },
//                onError = { error ->
//                    error.printStackTrace()
//                }
//            )
//        disposable.dispose()


        //
        class MyCallback : CommonCallback {

            var label = 0

            override fun onResult(result: Result<Any?>) {
                when (label) {
                    0 -> {
                        label++
                        HttpClient.request(1, this)
                    }
                    1 -> {
                        label++
                        val response = result.getOrThrow() as String
                        Dao.save(response, this)
                    }
                    2 -> {
                        label++
                        val id = result.getOrThrow() as Int
                        DownloadManager.downloadFile("xxx$id", this)
                    }
                    3 -> {
                        val file = result.getOrThrow() as File
                        println("file download success, ${file.path}")
                    }
                }
            }
        }


        try {
            val callback = MyCallback()
            // TODO RxJava 是通过操作符拼接把代码掰直的，那 coroutine 是如何把代码掰直的？
            // TODO 先是要通过实战代码直观反应出为什么 coroutines 好，即好用在哪里，有了 RxJava 为什么还要再引入 coroutine
            //   这次讲解下 coroutine 的挂起原理、kotlin.coroutine 和 kotlinx.coroutine 的关系，以及异常传播机制
            // TODO 这么几个问题：为什么不会阻塞主线程？（直接 return 了）
            // TODO 为什么 createCoroutine 只能传入无参数的 suspend lambda ？
            // TODO 为什么上面直接 onResume(Unit) 而这里则是 Result<Unit>?
            //  因为 Result 是内联类
            // TODO 为什么返回的是一个 Object，而不是 void？
            callback.onResult(Result.success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
        }


//        HttpClient.request(1, object : CommonCallback {
//            override fun onSuccess(response: Any?) {
//                Dao.save(response as String, object : CommonCallback {
//                    override fun onSuccess(id: Any?) {
//                        DownloadManager.downloadFile("xxx$id", object : CommonCallback {
//                            override fun onSuccess(file: Any?) {
//                                println("file download success, $file")
//                            }
//
//                            override fun onError(error: Exception) {
//                                error.printStackTrace()
//                            }
//                        })
//                    }
//
//                    override fun onError(error: Exception) {
//                        error.printStackTrace()
//                    }
//                })
//            }
//
//            override fun onError(error: Exception) {
//                error.printStackTrace()
//            }
//        })
    }
}