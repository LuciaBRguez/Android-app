package com.example.naturzaragoza.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.naturzaragoza.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val EXTRA_COMMON_NAME = "MapsActivity::commonName"
        const val EXTRA_LONGITUD = "MapsActivity::longitud"
        const val EXTRA_LATITUD = "MapsActivity::latitud"

        fun startMapActivity(context: Context, commonName: String, latitud: Double, longitud: Double) {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra(EXTRA_COMMON_NAME, commonName)
            intent.putExtra(EXTRA_LATITUD, latitud)
            intent.putExtra(EXTRA_LONGITUD, longitud)
            context.startActivity(intent)
        }
    }

    private lateinit var mMap: GoogleMap
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private var commonName: String = "arbol"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        commonName = intent.getStringExtra(EXTRA_COMMON_NAME)?: "ARBOL"
        latitud = intent.getDoubleExtra(EXTRA_LATITUD, 0.0)
        longitud = intent.getDoubleExtra(EXTRA_LONGITUD, 0.0)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker in Zaragoza.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in arbol and move the camera
        val arbol = LatLng(latitud, longitud)
        mMap.addMarker(MarkerOptions().position(arbol).title(commonName))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arbol, 16.0f))

    }
}
