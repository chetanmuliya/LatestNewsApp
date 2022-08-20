package learn.cm.latestnewsapp.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserRegisterEntity::class], version = 1)
abstract class RegisterDatabase: RoomDatabase(){

    abstract fun registerDatabaseDao(): UserRegistrationDao


    companion object {

        @Volatile
        private var INSTANCE: RegisterDatabase? = null

        fun getDatabase(context: Context): RegisterDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RegisterDatabase::class.java,
                    "User_Registration_Table"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}