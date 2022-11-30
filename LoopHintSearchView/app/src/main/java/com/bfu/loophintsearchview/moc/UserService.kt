package com.bfu.loophintsearchview.moc

import kotlinx.coroutines.delay

interface UserService {

    /**
     * 根据 [id] 获取 [User]
     */
    suspend fun getUserOrThrow(id: String): User

    companion object : UserService by UserServiceImpl
}

///////////////////////////////////////////////////////////////////////////
// UserService Impl
///////////////////////////////////////////////////////////////////////////

private object UserServiceImpl : UserService {

    override suspend fun getUserOrThrow(id: String): User {
        delay(2000)
        return User(id, "付博")
    }
}

