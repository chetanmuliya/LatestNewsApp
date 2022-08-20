package learn.cm.latestnewsapp.database.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserRegistrationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(registerEntity: UserRegisterEntity)

    @Query("SELECT * FROM user_registration_table ORDER BY userId DESC")
    fun getUsers(): LiveData<List<UserRegisterEntity>>

    @Query("SELECT * FROM user_registration_table WHERE userName LIKE :username")
    suspend fun getUserByUserName(username: String): UserRegisterEntity?
}