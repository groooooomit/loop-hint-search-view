package com.bfu.loophintsearchview.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.bfu.loophintsearchview.R
import com.bfu.loophintsearchview.databinding.DialogPrivacyBinding


class PrivacyDialog : DialogFragment(R.layout.dialog_privacy) {

    private val binding by lazy { DialogPrivacyBinding.bind(requireView()) }

    private val id by lazy { arguments?.getString(KEY_ID) }

    private val listenerHolder by viewModels<ListenerHolder>()

    /* onResultListener 需要用 ViewModel 来存储才不会在 Activity 重建时丢失. */
    var onResultListener: ((Boolean) -> Unit)?
        get() = listenerHolder.resultListener
        set(value) {
            listenerHolder.resultListener = value
        }

    class ListenerHolder : ViewModel() {
        var resultListener: ((Boolean) -> Unit)? = null
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /* content. */
        binding.txtContent.text = "是否同意 app 使用 $id 的信息?"

        /* bt grant */
        binding.btGrant.setOnClickListener {
            dismiss()
            onResultListener?.invoke(true)
        }

        /* bt denied */
        binding.btDenied.setOnClickListener {
            dismiss()
            onResultListener?.invoke(false)
        }
    }


    /**
     * config dialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {

            /* 干掉默认 title. */
            requestWindowFeature(Window.FEATURE_NO_TITLE)

            /* 不允许点击返回按钮关闭弹窗. */
            isCancelable = false

            /* 不允许点击外部区域关闭弹窗. */
            setCanceledOnTouchOutside(false)
        }

    /**
     * config window
     */
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {

            /* 设置动画. */
            setWindowAnimations(R.style.DialogTheme_AnimBottom)

            /* 如果不设置，window 布局属性将失效. */
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            /* 显示在底部. */
            setGravity(Gravity.BOTTOM)

            /* 设置 size. */
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }


    companion object {
        private const val KEY_ID = "id"
        fun newInstance(id: String): PrivacyDialog {
            val args = Bundle().apply {
                putString(KEY_ID, id)
            }
            val fragment = PrivacyDialog().apply {
                arguments = args
            }
            return fragment
        }
    }


}



