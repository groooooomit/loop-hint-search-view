package com.bfu.loophintsearchview


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.bfu.loophintsearchview.databinding.LayoutSearchViewBinding
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.flatMapSequence
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

/**
 * 首页搜索框（RxJava 版本）
 */
class RxLoopHintSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "RxLoopHintSearchView"
    }

    private val binding = LayoutSearchViewBinding.inflate(LayoutInflater.from(context), this, true)

//    private val hintListFlow = MutableLiveData(listOf<String>())

    private val hintListSubject = PublishSubject.create<List<String>>()

    /** View 生命周期 scope. */

    private var _viewLifecycleOwner: LifecycleOwner? by Delegates.observable(null) { _, oldValue, newValue ->
        (oldValue?.lifecycle as? LifecycleRegistry)?.currentState = Lifecycle.State.DESTROYED
        (newValue?.lifecycle as? LifecycleRegistry)?.currentState = Lifecycle.State.RESUMED


        newValue?.apply {
            hintListSubject.toFlowable(BackpressureStrategy.LATEST)
                .filter(Collection<String>::isNotEmpty) /* 过滤掉空数据. */
                .switchMap {
                    loopShowHints(it)
                }
                .toLiveData()
                .observe(this, Observer {})
        }

    }


    fun updateHint(hintItems: List<String>) {
//        hintListFlow.value = hintItems
        hintListSubject.onNext(hintItems)
    }

    init {
        binding.searchLayout.setOnClickListener {
            onClick("- EMPTY -", -1)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        _viewLifecycleOwner = object : LifecycleOwner {
            private val registry = LifecycleRegistry(this)
            override fun getLifecycle(): Lifecycle = registry
        }
    }

    override fun onDetachedFromWindow() {
        _viewLifecycleOwner = null
        super.onDetachedFromWindow()
    }

//    private fun loopShowHints(hints: List<String>): Completable =
//        hints.withIndex().map { (index, item) ->
//            showItemAnimatedly(index, item)
//        }.let {
//            Completable.concat(it)
//        }.repeat(if (hints.size <= 1) 1 else Long.MAX_VALUE)


    private fun loopShowHints(hints: List<String>) = Flowable.fromIterable(hints.withIndex())
        .concatMapSingle { (index, item) -> showItemAnimatedly(index, item).toSingle { 1 } }
        .doOnCancel {
            Log.i(TAG, "loopShowHints doOnCancel 1")
        }
        .onBackpressureLatest()
        .repeat(if (hints.size <= 1) 1 else Long.MAX_VALUE)


    private fun showItemAnimatedly(index: Int, item: String): Completable = Completable.defer {
        Completable.complete()
            .doOnComplete {
                /* nextHintText 在动画执行开始前更新 text. */
                binding.nextHintText.text = item
            }
            .andThen(
                Completable.merge(
                    listOf(
                        binding.nextHintText.slideIn(),
                        binding.preHintText.slideOut()
                    )
                )
            )
            .doOnComplete {
                /* preHintText 在动画执行结束后更新 text. */
                binding.preHintText.text = item
            }
            .doOnComplete {
                /* 更新点击事件. */
                binding.searchLayout.setOnClickListener {
                    onClick(item, index)
                }
            }
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun onClick(item: String, index: Int) {
        Toast.makeText(context, "item: $item, index: $index", Toast.LENGTH_SHORT).show()
    }

}


/**
 * View 滑动出去
 */
private fun View.slideOut(): Completable {
    translationY = 0f
    val animator = animate()
        .translationY(-(40.dp))
        .setDuration(1000)
    animator.start()
    return animator.asCompletable()
}

/**
 * View 滑动进入
 */
private fun View.slideIn(): Completable {
    translationY = 40.dp
    val animator = animate()
        .translationY(0f)
        .setDuration(1000)
    animator.start()
    return animator.asCompletable()
}

private fun <T> LiveData<T>.toFlowable(lifecycleOwner: LifecycleOwner): Flowable<T> =
    toPublisher(lifecycleOwner)
        .let { Flowable.fromPublisher(it) }

private fun <T> Flowable<T>.collectLatest(
    lifecycleOwner: LifecycleOwner,
    action: (T) -> CompletableSource
) {
    onBackpressureLatest()
        .flatMapCompletable(action)
        .toFlowable<Unit>()
        .toLiveData().observe(lifecycleOwner, Observer {})
}


