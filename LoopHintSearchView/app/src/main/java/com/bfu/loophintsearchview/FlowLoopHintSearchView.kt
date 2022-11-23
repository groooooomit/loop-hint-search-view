package com.bfu.loophintsearchview


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.bfu.loophintsearchview.databinding.LayoutSearchViewBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlin.properties.Delegates

/**
 * 首页搜索框
 */
class FlowLoopHintSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutSearchViewBinding.inflate(LayoutInflater.from(context), this, true)

    private val hintListFlow = MutableStateFlow(listOf<String>())

    /** View 生命周期 scope. */
    private var _viewScope: CoroutineScope? by Delegates.observable(null) { _, oldValue, newValue ->
        oldValue?.cancel()
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

    private suspend fun loopShowHints(hints: List<String>) = coroutineScope {

        /* 无限滚动处理数据，直到协程被取消. */
        while (isActive) {

            hints.forEachIndexed { index, item ->

                /* 进入循环体主动检查协程状态. */
                ensureActive()

                /* 切换到指定 item */
                showItemAnimatedly(index, item)

            }

            /* item 数量不足 2 个，那么不滚动播放 */
            if (hints.size <= 1) {
                /* 等着当前协程被取消，要么因为来新数据取消当前协程，要么因为页面结束取消当前协程 */
                awaitCancellation()
            }
        }
    }

    /* 新的数据不打断已经展示的数据的动画和展示过程，所以套上 NonCancellable */
    private suspend fun showItemAnimatedly(index: Int, item: String) = withContext(NonCancellable) {

        /* NonCancellable 会连同生命周期结束事件一起屏蔽，此处需要用 lifecycle 顶级 scope 新启一个协程 */
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
            delay(1000)

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
        .setDuration(1000)
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
        .setDuration(1000)
    animator.start()
    animator.awaitEnd()
}


