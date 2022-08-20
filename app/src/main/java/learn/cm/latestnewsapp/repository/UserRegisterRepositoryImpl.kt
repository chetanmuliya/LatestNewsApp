package learn.cm.latestnewsapp.repository

import android.util.Log
import learn.cm.latestnewsapp.database.local.RegisterDatabase
import learn.cm.latestnewsapp.database.local.UserRegisterEntity
import androidx.lifecycle.MutableLiveData
import learn.cm.latestnewsapp.database.remote.ApiInterface
import learn.cm.latestnewsapp.database.remote.model.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.LiveData




class UserRegisterRepositoryImpl(private val db: RegisterDatabase) {

    private var apiService: ApiInterface? = null
    private val newsliveData: MutableLiveData<NewsResponse?> = MutableLiveData()

    val users = db.registerDatabaseDao().getUsers()

    init {
        apiService = ApiInterface.create()
    }
    
    fun fetchNews(geo: String,country: String) {
        apiService?.getNews(geo = geo, country = country)?.enqueue(object :
            Callback<NewsResponse?> {
            override fun onResponse(call: Call<NewsResponse?>, response: Response<NewsResponse?>) {
                if (response.body() != null) {
                    newsliveData?.postValue(response.body())
                    Log.d("**********", "onFailure: ${newsliveData?.value}")
                }
            }

            override fun onFailure(call: Call<NewsResponse?>, t: Throwable) {
                newsliveData?.postValue(null)
            }

        })
    }

    fun getNewsResponseLiveData(): LiveData<NewsResponse?>? {
        return newsliveData
    }

    suspend fun insert(user: UserRegisterEntity) {
        return db.registerDatabaseDao().insertUser(user)
    }

    suspend fun getUserName(userName: String):UserRegisterEntity?{
        return db.registerDatabaseDao().getUserByUserName(userName)
    }

}