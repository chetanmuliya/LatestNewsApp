package learn.cm.latestnewsapp.presentation.news_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.navArgs
import kotlinx.coroutines.launch
import learn.cm.latestnewsapp.R
import learn.cm.latestnewsapp.database.data.datastore.PreferenceUtils
import learn.cm.latestnewsapp.databinding.ActivityNewsDetailWebViewBinding
import learn.cm.latestnewsapp.presentation.MainActivity
import learn.cm.latestnewsapp.presentation.news_detail.ui.home.HomeFragmentArgs
import learn.cm.latestnewsapp.util.Constants

class NewsDetailWebViewActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNewsDetailWebViewBinding

    companion object{
         const val WEB_LINK = "weblink"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsDetailWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarNewsDetailWebView.toolbar)

        binding.appBarNewsDetailWebView.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_news_detail_web_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        setUpUI()
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    private fun setUpUI() {

        val weblink = intent.getStringExtra(WEB_LINK) ?: ""
        if (weblink.isNotEmpty()){
            val bundle = bundleOf(WEB_LINK to weblink)
            navController.setGraph(R.navigation.mobile_navigation,bundle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.news_detail_web_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logOut() {
        lifecycleScope.launch {
            setLoggedIn()
        }
    }

    private fun setLoggedIn() {
        PreferenceUtils.putBoolean(Constants.SET_LOGGED_IN, false, this)
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_news_detail_web_view)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}