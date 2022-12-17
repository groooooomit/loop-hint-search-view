package com.bfu.loophintsearchview.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalAnimationApi
class ComposeLoopHintSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val hintListFlow = MutableStateFlow(listOf<String>())

    init {
        ComposeView(context)
            .apply {
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT,
                )
                setContent {
                    val hints by hintListFlow.collectAsState()
                    LoopHintSearch(hints) {
                        Toast.makeText(context, "hint: $it", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .apply(::addView)
    }

    fun updateHint(hintItems: List<String>) {
        hintListFlow.value = hintItems
    }

}