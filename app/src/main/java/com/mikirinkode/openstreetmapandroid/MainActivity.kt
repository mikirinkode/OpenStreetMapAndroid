package com.mikirinkode.openstreetmapandroid

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.mikirinkode.openstreetmapandroid.databinding.ActivityMainBinding
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainActivity : AppCompatActivity() {

    private val LOCATION_REQUEST_CODE = 0

    // init binding
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        managePermissions()

        setupMap()

    }

    private fun setupMap(){
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        mapView = binding.mapView
        mapController = mapView.controller

        mapView.setMultiTouchControls(true)

        // init the start point
        val startPoint = GeoPoint(-4.4911623643110685, 117.15596523066802)
        mapController.setCenter(startPoint)
        mapController.setZoom(6.0)

        // create marker
        val marker = Marker(mapView)
        marker.position = startPoint
        marker.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_location, null)

        // add marker to map view
        mapView.overlays.add(marker)
    }

    private fun managePermissions() {
        // check the permissions
        val requestPermissions = mutableListOf<String>()
        if (!isLocationPermissionGranted()){
            // if permissions are not granted
            requestPermissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (requestPermissions.isNotEmpty()){
            // request the permission
            ActivityCompat.requestPermissions(this, requestPermissions.toTypedArray(), LOCATION_REQUEST_CODE)
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        val fineLocation = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseLocation = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fineLocation && coarseLocation
    }

    // handle the request permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE){
            if (grantResults.isNotEmpty()){
                for (result in grantResults){
                    Toast.makeText(this, "Permissions are Granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}