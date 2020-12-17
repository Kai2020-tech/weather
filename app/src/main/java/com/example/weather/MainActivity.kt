package com.example.weather

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.inputmethod.InputMethodManager
//import android.widget.SearchView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weather.network.WeatherApi
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity(), ILocationPublisher {

    /** get location */

    private var PERMISSION_ID = 1000
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//    lateinit var locationRequest: LocationRequest

    companion object {
        var myLocation: String = ""
    }

    /** get location */

    val suggestions = listOf<String>()  //存放城市名,建議搜尋用

    var weatherList: MutableList<WeatherData.Records.Location> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//      get location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        requestPermission()
        getLastLocation()


        val pageAdapter = PageAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = pageAdapter
        viewPager2.setPageTransformer(ZoomOutPageTransformer())

//        呼叫此函數,讓非當前畫面的Second Fragment也先載入
//        在Second Fragment的subscribe才會在app啓動時就生效
        viewPager2.offscreenPageLimit = 1

//      viewpager2與tableLayout的title連動
        val title: ArrayList<String> = arrayListOf("今日", "明日")

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = title[position]
        }.attach()
//      call api..
        WeatherApi
            .retrofitService
            .getWeather(key = "rdec-key-123-45678-011121314")
            .enqueue(object : retrofit2.Callback<WeatherData> {
                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    //Use Timber
                    Log.e("fail", t.message)
                }

                override fun onResponse(
                    call: Call<WeatherData>,
                    response: Response<WeatherData>
                ) {
                    //api response..
                    Log.d("success", response.toString())
                    if (response.isSuccessful) {
                        response.body()?.records?.location?.forEach {
                            Log.d("api", it.toString())
                            weatherList.add(it)
                        }

                        weatherList.forEach { location ->
                            if (location.locationName.contains(myLocation)) {
                                setQueryResult(location)
                            }
                        }
                    }
                }
            })
// call api

    }

    //  api放在著呼叫,weatherList才有東西寫進去  爲何不能寫在onCreate
    override fun onStart() {
        super.onStart()

    }

/*    override fun onCreateOptionsMenu(menu: Menu) : Boolean{
        menuInflater.inflate(R.menu.menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

        searchView.findViewById<AutoCompleteTextView>(R.id.search_src_text).threshold = 1

        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter: SimpleCursorAdapter = SimpleCursorAdapter(this, R.layout.search_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        val suggestions = listOf("Apple", "Blueberry", "Carrot", "Daikon")

        searchView.suggestionsAdapter = cursorAdapter

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                query?.let {
                    suggestions.forEachIndexed { index, suggestion ->
                        if (suggestion.contains(query, true))
                            cursor.addRow(arrayOf(index, suggestion))
                    }
                }

                cursorAdapter.changeCursor(cursor)
                return true
            }
        })

        searchView.setOnSuggestionListener(object: androidx.appcompat.widget.SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                searchView.setQuery(selection, false)

                // Do something with selection
                return true
            }

        })

      return  super.onCreateOptionsMenu(menu)
    }*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    if (imm != null) {
                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                        imm.hideSoftInputFromWindow(
                            searchView.windowToken,
                            0
                        ) // 输入法如果是显示状态，那么就隐藏输入法
                    }

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        weatherList.forEach { location ->
                            val nText = newText.replace("台", "臺")
                            if (location.locationName.contains(nText)) {
                                /**origin*/
                                setQueryResult(location)
                                /**origin*/

//                                /**Aria*/
////                                val recyclerViewAdapter = viewPager2.adapter
////                                val pageAdapter = recyclerViewAdapter as PageAdapter
////                                pageAdapter.fragments.forEach {  }
//                                (viewPager2.adapter as PageAdapter).fragments.forEach {
//                                    if (it is FirstFragment) it.updateContent(location)
//                                    else if (it is SecondFragment) it.updateContent(location)
//                                }
//                                /**Aria*/
                            }
                        }
                    }
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }


    private var location: WeatherData.Records.Location? = null
    private val locationSubscribers = mutableListOf<() -> Unit>()

    private fun setQueryResult(location: WeatherData.Records.Location) {
        this.location = location
        locationSubscribers.forEach { it.invoke() }
    }

    override fun getCurrentLocationWeatherRecord(): WeatherData.Records.Location? {
        return this.location
    }

    override fun add(subscriber: () -> Unit) {
        locationSubscribers.add(subscriber)
    }

    override fun remove(subscriber: () -> Unit) {
        locationSubscribers.remove(subscriber)
    }


    /** get location */
    private fun checkPermission(): Boolean {
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if (
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        newLocationData()
                    } else {
                        Log.d("Debug:", "Your Location:" + location.longitude)
                        myLocation = getCityName(
                            location.latitude,
                            location.longitude
                        )


                    }
                }
            } else {
                Toast.makeText(this, "Please Turn on Your device Location", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            requestPermission()
        }


    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:", "your last last location: " + lastLocation.longitude.toString())
            myLocation = getCityName(
                lastLocation.latitude,
                lastLocation.longitude
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun newLocationData() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    fun getCityName(lat: Double, long: Double): String {
        var cityName: String = ""
        var countryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Address = geoCoder.getFromLocation(lat, long, 3)

        cityName = if (Address[0].adminArea.contains("台")) {
            Address[0].adminArea.replace("台", "臺", false)
        } else {
            Address[0].adminArea
        }
        countryName = Address[0].countryName
        Log.d("Debug:", "Your City: $cityName ; your Country $countryName")
        return cityName
    }

    //debug用,查看位置權限的取得狀態
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug:", "You have the Permission")
            }
        }
    }

    /** get location */


}




