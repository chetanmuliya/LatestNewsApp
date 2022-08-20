package learn.cm.latestnewsapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import learn.cm.latestnewsapp.R
import learn.cm.latestnewsapp.database.local.RegisterDatabase
import learn.cm.latestnewsapp.presentation.login.LoginFragment
import learn.cm.latestnewsapp.presentation.login.LoginViewModel
import learn.cm.latestnewsapp.presentation.login.LoginViewModelfactory
import learn.cm.latestnewsapp.presentation.register.RegisterFragment
import learn.cm.latestnewsapp.repository.UserRegisterRepositoryImpl


class MainActivity : AppCompatActivity() {

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpUI()
        setupViewModel()

    }

    private fun setUpUI() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
    }


    private fun setupViewModel() {
        val repository = UserRegisterRepositoryImpl(RegisterDatabase.getDatabase(this))
        val viewModelProviderFactory = LoginViewModelfactory(repository)
        loginViewModel = ViewModelProvider(this, viewModelProviderFactory).get(LoginViewModel::class.java)
    }
}