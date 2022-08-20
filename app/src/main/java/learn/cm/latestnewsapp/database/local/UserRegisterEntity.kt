package learn.cm.latestnewsapp.database.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User_Registration_Table")
data class UserRegisterEntity(

    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0,
    var firstName: String,
    var lastName: String,
    var userName: String,
    var password: String
)