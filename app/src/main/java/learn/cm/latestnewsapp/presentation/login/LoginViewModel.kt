package learn.cm.latestnewsapp.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import learn.cm.latestnewsapp.database.local.UserRegisterEntity
import learn.cm.latestnewsapp.database.remote.model.NewsResponse
import learn.cm.latestnewsapp.repository.UserRegisterRepositoryImpl

class LoginViewModel(private val repository: UserRegisterRepositoryImpl): ViewModel() {

    suspend fun checkLogin(username: String, password: String) : Boolean{
        val user = repository.getUserName(username)
        if (user != null){
            return user.password == password
        }
        return false
    }

    suspend fun registerUser(user: UserRegisterEntity){
        repository.insert(user)
    }

    val users = repository.users
}