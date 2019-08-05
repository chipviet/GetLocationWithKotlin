package com.example.getcurrentlocation

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val REQUEST_CODE = 1000;

        //Check permission
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
        else {
            buildLocationRequest()
            buildLocationCallBack()

            //Create FusedProviderClient
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            //set event
            btn_start_updates.setOnClickListener(View.OnClickListener {

                if (ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED )
                {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
                    return@OnClickListener
                }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())

                //change stare of button
                btn_start_updates.isEnabled = !btn_start_updates.isEnabled
                btn_stop_updates.isEnabled = !btn_stop_updates.isEnabled
            })

            btn_stop_updates.setOnClickListener(View.OnClickListener {
                if (ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED )
                {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
                    return@OnClickListener
                }
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)

                //change stare of button
                btn_start_updates.isEnabled = !btn_start_updates.isEnabled
                btn_stop_updates.isEnabled = !btn_stop_updates.isEnabled
            })

        }


    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {

                var location = p0!!.locations.get(p0!!.locations.size-1)
                txt_location.text = location.latitude.toString()+ "/"+location.longitude.toString()
            }

        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.smallestDisplacement = 10f
    }
}
