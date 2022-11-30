package com.bfu.loophintsearchview.moc

import kotlinx.coroutines.delay

interface UserDao {

    /**
     * 保存 [User]
     */
    suspend fun save(user: User)

    companion object : UserDao by UserDaoImpl
}


///////////////////////////////////////////////////////////////////////////
// UserDao impl
///////////////////////////////////////////////////////////////////////////

private object UserDaoImpl : UserDao {

    override suspend fun save(user: User) {
        delay(1000)
    }
}