package com.bfu.loophintsearchview.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bfu.loophintsearchview.moc.UserDao
import com.bfu.loophintsearchview.moc.UserService
import com.bfu.loophintsearchview.ui.awaitPrivacyGrantDialogResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class CoroutineUserViewModel : UserViewModel() {

    override val info = MutableLiveData<String?>(null)

    override val isLoading = MutableLiveData(false)

    override fun login(id: String, password: String) {
        viewModelScope.launch {
            info.value = null
            isLoading.value = true
            try {

                /* 参数检查. */
                val validId = id.takeIf { it.isNotEmpty() } ?: error("ID 不能为空")
                val validPwd = password.takeIf { it.isNotEmpty() } ?: error("密码不能为空")

                /* 用户授权. */
                info.value = "等待用户授权..."
                val grant = awaitPrivacyGrantDialogResult(id) ?: error("操作超时")
                if (!grant) error("用户未授权")

                /* 登录. */
                info.value = "用户已授权，登录中..."
                val user = UserService.loginOrThrow(validId, validPwd)

                /* 持久化. */
                UserDao.save(user)
                info.value = "登录成功：$user"

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                info.value = "登录异常: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    companion object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = CoroutineUserViewModel() as T
    }
}