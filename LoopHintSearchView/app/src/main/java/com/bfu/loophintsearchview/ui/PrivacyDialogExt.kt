package com.bfu.loophintsearchview.ui

import com.bfu.loophintsearchview.util.AppHelper
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

/**
 * 在当前处于 RESUMED 状态的页面（栈顶 Activity）弹出 [PrivacyDialog] 并挂起等待用户授权
 *
 * @param id 用户 ID
 * @param timeoutMillis 超时时间（超过该时间授权框将自动关闭）
 *
 * @return true 表示用户同意；false 表示用户拒绝；null 表示超时了
 */
suspend fun awaitPrivacyGrantDialogResult(id: String, timeoutMillis: Long = 5000): Boolean? =
    withTimeoutOrNull(timeoutMillis) {
        suspendCancellableCoroutine { cont ->
            val tag = "PrivacyDialog"
            val job = AppHelper.runOnceOnResumed {
                /* 弹出 Privacy 弹窗. */
                PrivacyDialog.newInstance(id)
                    .also { dialog ->
                        /* 等待 dialog 生命周期就绪时再设置 listener. */
                        dialog.viewLifecycleOwnerLiveData.observe(this) {
                            if (null != it) {
                                dialog.onResultListener = cont::resume
                            }
                        }
                    }
                    .show(supportFragmentManager, tag)
            }

            /* 外部协程取消时取消内部任务. */
            cont.invokeOnCancellation {
                job.cancel()
                /* 找到 dialog 并关闭. */
                AppHelper.runOnceOnResumed {
                    supportFragmentManager
                        .findFragmentByTag(tag)
                        .let { it as? PrivacyDialog }
                        ?.dismiss()
                }
            }
        }
    }


