package learn.cm.latestnewsapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import learn.cm.latestnewsapp.repository.UserRegisterRepository
import learn.cm.latestnewsapp.repository.UserRegisterRepositoryImpl

class HomeViewModelfactory(private val repository: UserRegisterRepositoryImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}