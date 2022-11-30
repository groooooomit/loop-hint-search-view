package com.bfu.loophintsearchview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.bfu.loophintsearchview.databinding.ActivityMainBinding
import com.bfu.loophintsearchview.ui.UserFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /* add fragment */
        supportFragmentManager.commit {
            replace(R.id.container, UserFragment::class.java, null)
        }
    }

}