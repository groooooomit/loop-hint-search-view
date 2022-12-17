package com.bfu.loophintsearchview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.bfu.loophintsearchview.databinding.ActivityMainBinding
import com.bfu.loophintsearchview.ui.SearchFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /* add target fragment if not exists */
        val page = supportFragmentManager.findFragmentById(R.id.container)
        if (null == page) {
            supportFragmentManager.commit {
                add(R.id.container, SearchFragment::class.java, null)
            }
        }
    }

}