package com.bfu.loophintsearchview.moc

import kotlinx.coroutines.delay

interface UserService {

    /**
     * 检查是否需要展示 Privacy 弹窗
     */
    suspend fun checkShowPrivacyDialogOrThrow(): Boolean

    /**
     * 根据 [id] 登录并返回 [User] 信息
     */
    suspend fun loginOrThrow(id: String, pwd: String): User

    companion object : UserService by UserServiceImpl
}

///////////////////////////////////////////////////////////////////////////
// UserService Impl
///////////////////////////////////////////////////////////////////////////

private object UserServiceImpl : UserService {

    override suspend fun checkShowPrivacyDialogOrThrow(): Boolean {
        delay(1000)
        return true
    }

    override suspend fun loginOrThrow(id: String, pwd: String): User {
        delay(2000)
        if (id != "111") error("用户不存在")
        if (pwd != "123456") error("密码不正确")
        return User(id, "付博")
    }
}

