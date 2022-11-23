package com.bfu.loophintsearchview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bfu.loophintsearchview.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mockData()
    }

    private fun mockData() {
        lifecycleScope.launch {
            while (isActive) {

                delay(2_000)

                val data0 = listOf("我是一个默认文案")
                binding.rxSearchView.updateHint(data0)
                binding.flowSearchView.updateHint(data0)

                delay(5_000)

                val data1 = listOf(1, 2, 3, 4, 5, 6, 7).map { it.toString().repeat(5) }
                binding.rxSearchView.updateHint(data1)
                binding.flowSearchView.updateHint(data1)

                delay(6_000)

                val data2 = listOf("A", "B", "C", "D", "E", "F", "G").map { it.repeat(5) }
                binding.rxSearchView.updateHint(data2)
                binding.flowSearchView.updateHint(data2)

                delay(10_000)
            }
        }
    }
}