package ru.podtynnikov.stepmeter.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.podtynnikov.stepmeter.R


class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    var locationManager:LocationManager?=null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        locationManager = this.activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        if (this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            this.activity?.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION),
                102)
        }
        locationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 10, 1000f, locationListener
        )
        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, 1000 * 10, 1000f,
            locationListener
        )
        locationManager?.requestLocationUpdates(
            LocationManager.FUSED_PROVIDER, 1000 * 10, 1000f,
            locationListener
        )
        locationManager?.requestLocationUpdates(
            LocationManager.PASSIVE_PROVIDER, 1000 * 10, 1000f,
            locationListener
        )
    }
    private val locationListener: LocationListener = object : LocationListener {
        override fun onProviderDisabled(provider: String) {
        }

        override fun onProviderEnabled(provider: String) {
        }

        override fun onLocationChanged(location: Location) {
            val you = LatLng(location.latitude, location.longitude)
            mMap!!.clear()
            mMap!!.addMarker(
                MarkerOptions()
                    .position(you)
                    .title("Вы")
            )
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(you,15f))

        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        }
    }
}

