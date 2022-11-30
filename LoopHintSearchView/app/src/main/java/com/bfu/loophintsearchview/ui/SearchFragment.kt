package com.bfu.loophintsearchview.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bfu.loophintsearchview.R
import com.bfu.loophintsearchview.databinding.FragmentSearchBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by lazy { FragmentSearchBinding.bind(requireView()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mockData()
    }

    private fun mockData() {
        lifecycleScope.launch {
            while (isActive) {

                delay(1_000)

                val data0 = listOf("我是一个默认文案")
                showData(data0)

                delay(4_000)

                val data1 = (0..2).map { it.toString().repeat(5) }
                showData(data1)

                delay(8_000)

                val data2 = ('A'..'Z').map { it.toString().repeat(5) }
                showData(data2)

                delay(6_000)
            }
        }
    }

    private fun showData(data: List<String>) {
        binding.rxSearchView.updateHint(data)
        binding.flowSearchView.updateHint(data)
    }
}