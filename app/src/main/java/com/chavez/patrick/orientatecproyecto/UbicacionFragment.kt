package com.chavez.patrick.orientatecproyecto

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class UbicacionFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val tecsupLocation = LatLng(-12.045923, -76.952188)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ubicacion, container, false)

        // Inicializar el MapView
        mapView = view.findViewById(R.id.mapview)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // Inicializar el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Comprobar permisos de ubicación
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            // Obtener la última ubicación conocida
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val userLocation = LatLng(it.latitude, it.longitude)
                    // Mover la cámara a la ubicación del usuario
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))
                    // Agregar un marcador en la ubicación de TECSUP
                    googleMap.addMarker(
                        MarkerOptions().position(tecsupLocation).title("TECSUP Santa Anita")
                    )
                    // Dibujar la ruta desde la ubicación del usuario a TECSUP
                    drawRoute(userLocation, tecsupLocation)
                }
            }
        } else {
            // Solicitar permisos si no están concedidos
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun drawRoute(userLocation: LatLng, destination: LatLng) {
        val apiKey = "YOUR_API_KEY" // Reemplaza esto con tu clave de API de Google Maps
        val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=${userLocation.latitude},${userLocation.longitude}&" +
                "destination=${destination.latitude},${destination.longitude}&" +
                "key=$apiKey"

        CoroutineScope(Dispatchers.IO).launch {
            val route = fetchRoute(url)
            route?.let {
                val polylineOptions = PolylineOptions()
                    .addAll(PolyUtil.decode(it))
                    .width(8f)
                    .color(requireContext().getColor(R.color.teal_200))
                // Agregar la polilínea al mapa
                googleMap.addPolyline(polylineOptions)
            }
        }
    }

    private suspend fun fetchRoute(url: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val jsonResponse = response.body?.string()
                val routes = JSONObject(jsonResponse ?: "").getJSONArray("routes")
                if (routes.length() > 0) {
                    return routes.getJSONObject(0)
                        .getJSONObject("overview_polyline").getString("points")
                }
            }
        }
        return null
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Volver a llamar a onMapReady si se concede el permiso
            onMapReady(googleMap)
        }
    }
}
