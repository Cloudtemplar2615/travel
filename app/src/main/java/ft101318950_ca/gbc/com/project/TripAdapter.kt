package ft101318950_ca.gbc.com.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TripAdapter(
    private var trips: MutableList<Trip>,
    private val onItemClick: (Trip) -> Unit
) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = trips[position]

        // Set trip data to the views
        holder.tripName.text = trip.name
        holder.tripDate.text = trip.date
        holder.tripDescription.text = trip.description

        // Set an image or map preview
        if (trip.latitude != 0.0 && trip.longitude != 0.0) {
            val staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=${trip.latitude},${trip.longitude}&zoom=12&size=300x150&key=YOUR_API_KEY"
            Glide.with(holder.itemView.context)
                .load(staticMapUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.tripImage)
        } else {
            Glide.with(holder.itemView.context)
                .load(R.drawable.placeholder_image) // Default placeholder image
                .into(holder.tripImage)
        }

        // Handle click event
        holder.itemView.setOnClickListener {
            onItemClick(trip)
        }
    }

    override fun getItemCount(): Int = trips.size

    fun updateList(newList: List<Trip>) {
        trips.clear()
        trips.addAll(newList)
        notifyDataSetChanged()
    }

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tripName: TextView = itemView.findViewById(R.id.trip_name)
        val tripDate: TextView = itemView.findViewById(R.id.trip_date)
        val tripDescription: TextView = itemView.findViewById(R.id.trip_description)
        val tripImage: ImageView = itemView.findViewById(R.id.trip_image)
    }
}







