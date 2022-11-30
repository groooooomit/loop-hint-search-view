package com.bfu.loophintsearchview.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bfu.loophintsearchview.R
import com.bfu.loophintsearchview.databinding.FragmentUserBinding
import com.bfu.loophintsearchview.viewmodel.CoroutineUserViewModel
import com.bfu.loophintsearchview.viewmodel.UserViewModel

class UserFragment : Fragment(R.layout.fragment_user) {

    private val binding by lazy { FragmentUserBinding.bind(requireView()) }

    private val userViewModel by viewModels<UserViewModel> { CoroutineUserViewModel }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /* loading 状态. */
        userViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.btLogin.isEnabled = !it
            binding.editId.isEnabled = !it
            binding.editPassword.isEnabled = !it
            binding.loading.isVisible = it
        }

        /* info */
        userViewModel.info.observe(viewLifecycleOwner) {
            binding.txtMsg.text = it
        }

        /* 点击. */
        binding.btLogin.setOnClickListener {
            val id = binding.editId.text.toString()
            val password = binding.editPassword.text.toString()
            userViewModel.login(id, password)
        }
    }


}