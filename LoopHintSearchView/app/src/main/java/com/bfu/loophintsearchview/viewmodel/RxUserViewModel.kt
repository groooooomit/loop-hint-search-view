package com.bfu.loophintsearchview.viewmodel

import android.graphics.Color
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bfu.loophintsearchview.moc.UserDao
import com.bfu.loophintsearchview.moc.UserService
import com.bfu.loophintsearchview.ui.awaitPrivacyGrantDialogResult
import com.bfu.loophintsearchview.util.SafelyMutableLiveData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.rx2.rxCompletable
import kotlinx.coroutines.rx2.rxSingle

class RxUserViewModel : UserViewModel() {

    override val info = SafelyMutableLiveData<CharSequence?>(null)

    override val isLoading = SafelyMutableLiveData(false)

    private var disposable: Disposable? = null

    override fun login(id: String, password: String) {
        disposable = Single.fromCallable {
            /* 参数检查. */
            val validId = id.takeIf { it.isNotEmpty() } ?: error("ID 不能为空")
            val validPwd = password.takeIf { it.isNotEmpty() } ?: error("密码不能为空")
            validId to validPwd
        }.doOnSubscribe {
            /* reset state. */
            info.value = null
            isLoading.value = true
        }.doOnSuccess {
            info.value = "正在检查是否需要用户授权..."
        }.flatMap { pair ->
            rxSingle {
                UserService.checkShowPrivacyDialogOrThrow()
            }.flatMap { needShow ->
                if (needShow) {
                    /* 用户授权. */
                    Completable.fromAction {
                        info.value = "等待用户授权..."
                    }.andThen(rxCompletable {
                        val grant = awaitPrivacyGrantDialogResult(id) ?: error("操作超时")
                        if (!grant) error("用户拒绝授权")
                    }.toSingle { pair })
                } else {
                    /* 不用授权，直接放行 */
                    Single.just(pair)
                }
            }
        }.doOnSuccess {
            /* 登录. */
            info.value = "用户已授权，登录中..."
        }.flatMap {
            rxSingle { UserService.loginOrThrow(it.first, it.second) }
        }.doOnSuccess {
            /* 持久化. */
            info.value = "持久化..."
        }.flatMap {
            rxCompletable { UserDao.save(it) }.toSingle { it }
        }.doOnDispose {
            // cancel
        }.doFinally {
            isLoading.value = false
        }.subscribeBy(
            onSuccess = { user ->
                // done
                info.value = buildSpannedString {
                    color(Color.GREEN) {
                        append("登录成功：$user")
                    }
                }
            },
            onError = { e ->
                // error
                info.value = buildSpannedString {
                    color(Color.RED) {
                        append("登录异常: ${e.message}")
                    }
                }
            }
        )
    }

    override fun onCleared() {
        disposable?.dispose()
        disposable = null
    }

    companion object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = RxUserViewModel() as T
    }
}