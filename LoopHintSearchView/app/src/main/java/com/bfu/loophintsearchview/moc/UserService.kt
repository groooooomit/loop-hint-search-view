package com.bfu.loophintsearchview.moc

import kotlinx.coroutines.delay

interface UserService {

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

    override suspend fun loginOrThrow(id: String, pwd: String): User {
        delay(2000)
        if (id != "111") error("用户不存在")
        if (pwd != "123456") error("密码不正确")
        return User(id, "付博")
    }
}

