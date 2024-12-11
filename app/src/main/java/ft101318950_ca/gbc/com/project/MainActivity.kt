package ft101318950_ca.gbc.com.project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var tripAdapter: TripAdapter
    private val tripList = mutableListOf<Trip>()
    private val allTrips = mutableListOf<Trip>() // New list for original data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        tripAdapter = TripAdapter(tripList) { trip ->
            openTripDetails(trip)
        }
        recyclerView.adapter = tripAdapter

        // Initialize SearchView
        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTrips(newText.orEmpty())
                return true
            }
        })

        // Initialize Sort Spinner
        val sortSpinner: Spinner = findViewById(R.id.sortSpinner)
        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> sortTripsByDate()
                    1 -> sortTripsByDestination()
                }
                tripAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Initialize Floating Action Button
        findViewById<View>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, AddTripActivity::class.java))
        }

        loadTrips()
    }

    override fun onResume() {
        super.onResume()
        loadTrips()
    }

    private fun loadTrips() {
        val sharedPreferences = getSharedPreferences("trips", MODE_PRIVATE)
        val tripsJson = sharedPreferences.getString("trips_list", null)

        if (!tripsJson.isNullOrEmpty()) {
            val savedTrips: List<Trip> = Trip.fromJsonArray(tripsJson)
            allTrips.clear()
            allTrips.addAll(savedTrips)
            tripList.clear()
            tripList.addAll(allTrips)
            tripAdapter.notifyDataSetChanged()
        }
    }

    private fun filterTrips(query: String) {
        tripList.clear()
        if (query.isEmpty()) {
            tripList.addAll(allTrips)
        } else {
            val filtered = allTrips.filter { trip ->
                trip.name.contains(query, ignoreCase = true) || trip.tags.contains(query, ignoreCase = true)
            }
            tripList.addAll(filtered)
        }
        tripAdapter.notifyDataSetChanged()
    }

    private fun sortTripsByDate() {
        tripList.sortBy { it.date }
    }

    private fun sortTripsByDestination() {
        tripList.sortBy { it.name }
    }

    private fun openTripDetails(trip: Trip) {
        val intent = Intent(this, TripDetailsActivity::class.java)
        intent.putExtra("trip", trip.toJson())
        startActivity(intent)
    }
}











