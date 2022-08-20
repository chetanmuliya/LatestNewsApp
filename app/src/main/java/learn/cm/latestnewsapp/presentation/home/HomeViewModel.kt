package learn.cm.latestnewsapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import learn.cm.latestnewsapp.database.local.UserRegisterEntity
import learn.cm.latestnewsapp.database.remote.model.NewsResponse
import learn.cm.latestnewsapp.repository.UserRegisterRepositoryImpl

class HomeViewModel(private val repository: UserRegisterRepositoryImpl): ViewModel() {

    private var newsliveData: LiveData<NewsResponse?>? = null

    init {
        newsliveData = repository.getNewsResponseLiveData()
    }

    suspend fun fetchNews(geo: String, country: String){
        repository.fetchNews(geo,country)
    }
    fun getNewsLiveData() = newsliveData

}