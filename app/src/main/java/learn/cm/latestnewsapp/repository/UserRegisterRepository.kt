package learn.cm.latestnewsapp.repository

import learn.cm.latestnewsapp.database.local.UserRegisterEntity

interface UserRegisterRepository {
    suspend fun insert(user: UserRegisterEntity)
    suspend fun getUserName(userName: String):UserRegisterEntity?
}