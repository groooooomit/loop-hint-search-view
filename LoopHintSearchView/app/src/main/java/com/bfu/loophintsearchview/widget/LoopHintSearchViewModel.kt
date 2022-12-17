package com.bfu.loophintsearchview.widget

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.map

class LoopHintSearchViewModel : ViewModel() {
    private val _hintListFlow = MutableStateFlow(listOf<String>())

    val hintListFlow = _hintListFlow.map {
        it.asFlow()
    }.flattenConcat()

    fun updateHint(hintItems: List<String>) {
        _hintListFlow.value = hintItems
    }
}