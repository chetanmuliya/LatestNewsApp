package learn.cm.latestnewsapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import learn.cm.latestnewsapp.presentation.login.LoginViewModel
import learn.cm.latestnewsapp.repository.UserRegisterRepository
import learn.cm.latestnewsapp.repository.UserRegisterRepositoryImpl

class RegisterViewModelFactory(private val repository: UserRegisterRepositoryImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}