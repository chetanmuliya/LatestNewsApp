package learn.cm.latestnewsapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import learn.cm.latestnewsapp.repository.UserRegisterRepository
import learn.cm.latestnewsapp.repository.UserRegisterRepositoryImpl

class LoginViewModelfactory(private val repository: UserRegisterRepositoryImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}