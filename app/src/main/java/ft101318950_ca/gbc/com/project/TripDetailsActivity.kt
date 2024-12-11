package ft101318950_ca.gbc.com.project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class TripDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var trip: Trip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        setContentView(R.layout.activity_trip_details)

        // Retrieve trip details from intent
        val tripJson = intent.getStringExtra("trip") ?: return
        trip = Trip.fromJson(tripJson)

        // Set trip details to TextViews
        findViewById<TextView>(R.id.textDestination).text = trip.name
        findViewById<TextView>(R.id.textDate).text = trip.date
        findViewById<TextView>(R.id.textNotes).text = trip.description
        findViewById<TextView>(R.id.textTags).text = trip.tags

        // Initialize MapView
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // Set up delete button functionality
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            deleteTrip()
        }

        // Set up share button functionality
        findViewById<Button>(R.id.btnShare).setOnClickListener {
            shareTrip()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        // Default to college location if latitude/longitude are 0.0
        val defaultLocation = LatLng(43.6532, -79.3832) // Example: Toronto
        val destinationLocation = if (trip.latitude == 0.0 && trip.longitude == 0.0) {
            defaultLocation
        } else {
            LatLng(trip.latitude, trip.longitude)
        }

        googleMap.addMarker(MarkerOptions().position(destinationLocation).title("Destination"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLocation, 10f))

        // Add "Tap to Edit" functionality
        googleMap.setOnMapClickListener { latLng ->
            trip.latitude = latLng.latitude
            trip.longitude = latLng.longitude

            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(latLng).title("Updated Location"))
            Toast.makeText(
                this,
                "Location updated to: ${latLng.latitude}, ${latLng.longitude}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun deleteTrip() {
        val sharedPreferences = getSharedPreferences("trips", MODE_PRIVATE)
        val tripsJson = sharedPreferences.getString("trips_list", null)

        if (!tripsJson.isNullOrEmpty()) {
            val currentTrips = Trip.fromJsonArray(tripsJson).toMutableList()
            val updatedTrips = currentTrips.filter { it.name != trip.name || it.date != trip.date }

            sharedPreferences.edit()
                .putString("trips_list", Trip.toJsonArray(updatedTrips))
                .apply()

            Toast.makeText(this, "Trip Deleted!", Toast.LENGTH_SHORT).show()
            finish() // Return to MainActivity
        } else {
            Toast.makeText(this, "No trips found to delete.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareTrip() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out my trip to ${trip.name} on ${trip.date}! Notes: ${trip.description}"
            )
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share Trip Details"))
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
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
}







