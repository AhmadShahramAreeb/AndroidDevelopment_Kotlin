package com.example.travel_book_kotlin.view
import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.travel_book_kotlin.R
import com.example.travel_book_kotlin.databinding.ActivityGoogleMapsBinding
import com.example.travel_book_kotlin.model.Place
import com.example.travel_book_kotlin.roomdb.PlaceDao
import com.example.travel_book_kotlin.roomdb.PlaceDatabase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding
    //kullanıcın konumunu almak için LocationManager ve LocationListener kull. gerek
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener : LocationListener
    private lateinit var permissionLauncher : ActivityResultLauncher<String>  /*Manifest'te izinler string olarak saklanir */
    private lateinit var sharedPreferences : SharedPreferences
    private var trackBoolean : Boolean? = null
    private var selectedLatitude :Double?=null
    private var selectedLongtitude :Double?=null
    private lateinit var db : PlaceDatabase
    private lateinit var placeDao : PlaceDao
    val compositeDisposable = CompositeDisposable()
    var placeFromMain : Place? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        registerLauncher()
        sharedPreferences = this.getSharedPreferences("com.example.travel_book_kotlin", MODE_PRIVATE)
        trackBoolean = false
        selectedLatitude = 0.0
        selectedLongtitude = 0.0

        db = Room.databaseBuilder(applicationContext,PlaceDatabase::class.java,"Places")
            //.allowMainThreadQueries()
            .build()


        placeDao = db.placeDao()

        binding.saveButton.isEnabled = false
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(this)

        val intent = intent
        val info = intent.getStringExtra("info")

        if (info == "new"){
            binding.saveButton.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.GONE

            locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
                    locationListener = object : LocationListener{
                        override fun onLocationChanged(location: Location) {
                            trackBoolean = sharedPreferences.getBoolean("trackBoolean",false)
                            if (trackBoolean == false){
                                val userLocation = LatLng(location.latitude,location.longitude)
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15f))
                                sharedPreferences.edit().putBoolean("trackBoolean",true).apply()
                            }


                        }
                    }

                    if (ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                            Snackbar.make(binding.root,"Permission Required For Map Usages",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                                // request permission
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

                            }.show()
                        } else {
                            // request permission
                                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    } else {
                        //permission granted
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
                        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        if (lastLocation != null){
                            val lastUserLocation = LatLng(lastLocation.latitude,lastLocation.longitude)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15f))
                        }
                        mMap.isMyLocationEnabled = true // Bilinen anlık kullanıcı konumunu gösteren mavi simge
                    }
        } else {
            mMap.clear()
            placeFromMain = intent.getSerializableExtra("selectedPlace") as? Place

            placeFromMain?.let {
                val latlng =LatLng(it.latitude,it.longitude)

                mMap.addMarker(MarkerOptions().position(latlng).title(it.name))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,15f))
                binding.placeText.setText(it.name)
                binding.saveButton.visibility = View.GONE
                binding.deleteButton.visibility = View.VISIBLE
            }
        }

        /*        //latitude-enlem ->48.858413899581635,   longitude-boylam-> 2.294467658090194
                // Add a marker in Sydney and move the camera
        val eiffel = LatLng(48.858413899581635,2.294467658090194)
        mMap.addMarker(MarkerOptions().position(eiffel))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eiffel,15f*//*1 ile 25 arasi*//*)) */

        //get User current location
        // type casting technique


    }

    private fun registerLauncher(){
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result->
            if (result){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED){
                    //permission granted
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
                    val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (lastLocation != null){
                        val lastUserLocation = LatLng(lastLocation.latitude,lastLocation.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15f))

                    }
                    mMap.isMyLocationEnabled = true // Bilinen anlık kullanıcı konumunu gösteren mavi simge

                }

            } else {
                //permission denied
                Toast.makeText(this@GoogleMapsActivity,"Permission needed",Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onMapLongClick(p0: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(p0))
        selectedLatitude = p0.latitude
        selectedLongtitude = p0.longitude

        binding.saveButton.isEnabled = true
    }

    fun save(view:View){
        // Main Thread UI,Default -> CPU, IO Thread -> Internet/Dtabase

        if(selectedLongtitude !=null && selectedLatitude != null){
            val place = Place(binding.placeText.text.toString(),selectedLatitude!!,selectedLongtitude!!)
            //placeDao.insert(place) rxjava
            compositeDisposable.add(
                placeDao.insert(place)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse /* :: ile referans verildi*/)
            )
        }
    }

    private fun handleResponse(){
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun delete(view:View){
        placeFromMain?.let {
            compositeDisposable.add(
                placeDao.delete(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse)
                    )
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}