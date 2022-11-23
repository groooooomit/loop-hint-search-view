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
                showData(data0)

                delay(5_000)

                val data1 = (0..100).map { it.toString().repeat(5) }
                showData(data1)

                delay(6_000)

                val data2 = ('A'..'Z').map { it.toString().repeat(5) }
                showData(data2)

                delay(10_000)
            }
        }
    }

    private fun showData(data: List<String>) {
        binding.rxSearchView.updateHint(data)
        binding.flowSearchView.updateHint(data)
    }
}