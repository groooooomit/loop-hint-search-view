package com.bfu.loophintsearchview.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class UserViewModel : ViewModel() {

    abstract val info: LiveData<String?>

    abstract val isLoading: LiveData<Boolean>

    abstract fun login(id: String, password: String)

}