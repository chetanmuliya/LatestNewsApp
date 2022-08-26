package learn.cm.latestnewsapp.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch
import learn.cm.latestnewsapp.R
import learn.cm.latestnewsapp.database.local.RegisterDatabase
import learn.cm.latestnewsapp.database.remote.model.Article
import learn.cm.latestnewsapp.databinding.ActivityHomeBinding
import learn.cm.latestnewsapp.presentation.home.adapter.NewsAdapter
import learn.cm.latestnewsapp.repository.UserRegisterRepositoryImpl
import android.provider.Settings
import android.os.Looper
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

import com.google.android.gms.location.LocationCallback
import learn.cm.latestnewsapp.database.data.datastore.PreferenceUtils
import learn.cm.latestnewsapp.database.remote.ApiInterface
import learn.cm.latestnewsapp.presentation.MainActivity
import learn.cm.latestnewsapp.presentation.news_detail.NewsDetailWebViewActivity
import learn.cm.latestnewsapp.util.Constants
import learn.cm.latestnewsapp.util.DRK
import java.util.*


private  const val  PERMISSION_ID = 44

class HomeActivity : AppCompatActivity(), NewsAdapter.Companion.OnItemClickListener {

    init {
        System.loadLibrary("native-lib")
    }
    lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: HomeViewModel
    private lateinit var adapter: NewsAdapter
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var cityName: String = ""
    private var countryCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)

        checkLogin()
        setupViewModel()
        requestLocationPermission()

    }

    private fun checkLogin() {
        val isloggedIn = PreferenceUtils.getBoolean(Constants.SET_LOGGED_IN,false,this)
        if (!isloggedIn){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun requestLocationPermission() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient?.lastLocation?.addOnCompleteListener { p0 ->
                    val location: Location? = p0.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        getAdress(location.longitude,location.latitude)
                        fetchNews()
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun getAdress(longitude: Double, latitude: Double)  {
        val geocoder = Geocoder(this, Locale.getDefault())
        val address: Address?

        val addresses: List<Address>? =
            geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses?.isNotEmpty() == true) {
            address = addresses[0]
            cityName = address.locality
            val country = address.countryCode
            val state = address.adminArea
            if (cityName.isEmpty()){
                if (state.isNotEmpty())
                    cityName = state
            }
            countryCode = country

        } else{
            countryCode = "IN"
            cityName = "Maharashtra"
        }
    }

    private fun fetchNews() {

        //getAdress(19.0760,72.8777)
        lifecycleScope.launch {
            viewModel.fetchNews(cityName,countryCode)
        }

        viewModel.getNewsLiveData()?.observe(this, Observer {
            binding.isListLoaded = true
            adapter.updateItems(it?.articles)
        })

    }

    private fun setupViewModel() {
        val repository = UserRegisterRepositoryImpl(RegisterDatabase.getDatabase(this))
        val viewModelProviderFactory = HomeViewModelfactory(repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)

        binding.isListLoaded = false
        //setup adapter
        adapter = NewsAdapter(this)
        binding.rvNews.adapter = adapter

    }

    override fun onItemClick(model: Article) {
        val intent = Intent(this,NewsDetailWebViewActivity::class.java)
        intent.putExtra(NewsDetailWebViewActivity.WEB_LINK,model.link)
        startActivity(intent)
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            /*val mLastLocation = locationResult.lastLocation
            latitudeTextView.setText("Latitude: " + mLastLocation.latitude + "")
            longitTextView.setText("Longitude: " + mLastLocation.longitude + "")*/
        }
    }
}