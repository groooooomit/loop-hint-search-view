package com.bfu.loophintsearchview.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.bfu.loophintsearchview.base.App
import com.bfu.loophintsearchview.base.dp
import com.bfu.loophintsearchview.databinding.LayoutSearchViewBinding
import com.bfu.loophintsearchview.util.asCompletable
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

/**
 * 首页搜索框（RxJava 版本）
 */
class RxDisposableLoopHintSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutSearchViewBinding.inflate(LayoutInflater.from(context), this, true)

    private val hintListFlow = PublishSubject.create<List<String>>()

    private var _disposable: Disposable? = null

    /** View 生命周期 lifecycleOwner. */
    private var _isViewActive: Boolean by Delegates.observable(false) { _, _, newValue ->
        /* 旧的 lifecycleOwner 狗带. */
        _disposable?.dispose()
        /* 开启新任务. */
        if (newValue) {
            _disposable = hintListFlow
                .filter(Collection<String>::isNotEmpty) /* 过滤掉空数据. */
                .toFlowable(BackpressureStrategy.LATEST)
                .switchMapCompletable(::loopShowHints)
                .subscribe() /* 订阅. */
        }
    }

    fun updateHint(hintItems: List<String>) {
        hintListFlow.onNext(hintItems)
    }

    init {
        binding.searchLayout.setOnClickListener {
            onClick("- EMPTY -", -1)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        _isViewActive = true
    }

    override fun onDetachedFromWindow() {
        _isViewActive = false
        super.onDetachedFromWindow()
    }

    private fun loopShowHints(hints: List<String>): Completable =
        Observable.fromIterable(hints.withIndex())
            .concatMapCompletable { (index, item) -> showItemAnimatedly(index, item) }
            .repeat(if (hints.size <= 1) 1 else Long.MAX_VALUE)


    private fun showItemAnimatedly(index: Int, item: String): Completable = Completable.complete()
        .doOnComplete {
            /* nextHintText 在动画执行开始前更新 text. */
            binding.nextHintText.text = item
        }
        .andThen(
            /* 同时执行动画. */
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
        .delay(App.ITEM_SHOWING_DURATION, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())


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
        .setDuration(App.ANIM_DURATION)
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
        .setDuration(App.ANIM_DURATION)
    animator.start()
    return animator.asCompletable()
}


