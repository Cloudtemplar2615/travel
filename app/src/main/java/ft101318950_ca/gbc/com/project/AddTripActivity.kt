package ft101318950_ca.gbc.com.project

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class AddTripActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

        val editDestination: EditText = findViewById(R.id.editDestination)
        val editDate: EditText = findViewById(R.id.editDate)
        val editNotes: EditText = findViewById(R.id.editNotes)
        val editTags: EditText = findViewById(R.id.editTags)
        val editLatitude: EditText = findViewById(R.id.editLatitude)
        val editLongitude: EditText = findViewById(R.id.editLongitude)
        val btnSave: Button = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            val destination = editDestination.text.toString().trim()
            val date = editDate.text.toString().trim()
            val notes = editNotes.text.toString()
            val tags = editTags.text.toString()
            val latitude = editLatitude.text.toString().toDoubleOrNull() ?: 0.0
            val longitude = editLongitude.text.toString().toDoubleOrNull() ?: 0.0

            if (destination.isBlank() || date.isBlank()) {
                Toast.makeText(this, "Please provide a destination and date.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!date.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                Toast.makeText(this, "Invalid date format. Use YYYY-MM-DD.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (latitude !in -90.0..90.0 || longitude !in -180.0..180.0) {
                Toast.makeText(this, "Invalid latitude or longitude values.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newTrip = Trip(destination, date, notes, tags, latitude, longitude)
            saveTrip(newTrip)
            Toast.makeText(this, "Trip Saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun saveTrip(trip: Trip) {
        val sharedPreferences = getSharedPreferences("trips", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val currentTripsJson = sharedPreferences.getString("trips_list", null)
        val currentTrips = if (!currentTripsJson.isNullOrEmpty()) {
            Trip.fromJsonArray(currentTripsJson).toMutableList()
        } else {
            mutableListOf()
        }

        currentTrips.add(trip)
        editor.putString("trips_list", JSONArray(currentTrips.map { it.toJson() }).toString())
        editor.apply()
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }
}








