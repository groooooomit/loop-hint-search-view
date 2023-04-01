package com.bfu.loophintsearchview.widget


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.bfu.loophintsearchview.util.awaitEnd
import com.bfu.loophintsearchview.base.App
import com.bfu.loophintsearchview.base.dp
import com.bfu.loophintsearchview.databinding.LayoutSearchViewBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlin.properties.Delegates

/**
 * 首页搜索框（Flow 版本）
 */
class FlowLoopHintSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutSearchViewBinding.inflate(LayoutInflater.from(context), this, true)

    private val hintListFlow = MutableStateFlow(listOf<String>())

    /** View 生命周期 scope. */
    private var _viewScope: CoroutineScope? by Delegates.observable(null) { _, oldValue, newValue ->
        /* 旧的 scope 狗带. */
        oldValue?.cancel()
        /* 开启新任务. */
        newValue?.launch {
            hintListFlow
                .filter(Collection<String>::isNotEmpty) /* 过滤掉空数据. */
                .collectLatest(::loopShowHints) /* 订阅. */
        }
    }

    fun updateHint(hintItems: List<String>) {
        hintListFlow.value = hintItems
    }

    init {
        binding.searchLayout.setOnClickListener {
            onClick("- EMPTY -", -1)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        _viewScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }

    override fun onDetachedFromWindow() {
        _viewScope = null
        super.onDetachedFromWindow()
    }

    private suspend fun loopShowHints(hints: List<String>) {
        repeat(if (hints.size <= 1) 1 else Int.MAX_VALUE) {
            hints.forEachIndexed { index, item ->
                /* 进入循环体主动检查协程状态. */
                currentCoroutineContext().ensureActive()
                /* 切换到指定 item */
                showItemAnimatedly(index, item)
            }
        }
    }

    private suspend fun showItemAnimatedly(index: Int, item: String) = withContext(NonCancellable) {

        _viewScope?.launch {

            /* nextHintText 在动画执行开始前更新 text. */
            binding.nextHintText.text = item

            /* 新的 hint 从视图区域下方移进来. */
            val nextAnim = launch {
                binding.nextHintText.slideIn()
            }

            /* 当前的 hint 从视图区域向上移出去. */
            val preAnim = launch {
                binding.preHintText.slideOut()
            }

            /* 等待俩动画全部结束. */
            joinAll(preAnim, nextAnim)

            /* preHintText 在动画执行结束后更新 text. */
            binding.preHintText.text = item

            /* 更新点击事件. */
            binding.searchLayout.setOnClickListener {
                onClick(item, index)
            }

            /* 让新进入的数据静静展示一段时间. */
            delay(App.ITEM_SHOWING_DURATION)
        }?.join()

    }

    private fun onClick(item: String, index: Int) {
        Toast.makeText(context, "item: $item, index: $index", Toast.LENGTH_SHORT).show()
    }

}


/**
 * View 滑动出去
 */
private suspend fun View.slideOut() {
    translationY = 0f
    val animator = animate()
        .translationY(-(40.dp))
        .setDuration(App.ANIM_DURATION)
    animator.start()
    animator.awaitEnd()
}

/**
 * View 滑动进入
 */
private suspend fun View.slideIn() {
    translationY = 40.dp
    val animator = animate()
        .translationY(0f)
        .setDuration(App.ANIM_DURATION)
    animator.start()
    animator.awaitEnd()
}


