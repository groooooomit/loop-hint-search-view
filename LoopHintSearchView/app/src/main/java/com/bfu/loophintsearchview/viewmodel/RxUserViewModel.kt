package com.bfu.loophintsearchview.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bfu.loophintsearchview.moc.UserDao
import com.bfu.loophintsearchview.moc.UserService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class RxUserViewModel : UserViewModel() {

    override val info = MutableLiveData<String?>(null)

    override val isLoading = MutableLiveData(false)

    override fun login(id: String, password: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val user = UserService.loginOrThrow(id, password)
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
        override fun <T : ViewModel> create(modelClass: Class<T>): T = RxUserViewModel() as T
    }
}