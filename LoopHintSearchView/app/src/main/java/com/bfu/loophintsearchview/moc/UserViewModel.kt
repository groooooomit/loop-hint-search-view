package com.bfu.loophintsearchview.moc

import androidx.lifecycle.ViewModel

abstract class UserViewModel : ViewModel() {

    abstract fun loadUser(id: String)

}