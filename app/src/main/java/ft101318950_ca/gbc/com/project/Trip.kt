package ft101318950_ca.gbc.com.project

import org.json.JSONArray
import org.json.JSONObject
import kotlinx.parcelize.Parcelize
import android.os.Parcelable


@Parcelize
data class Trip(
    val name: String,
    val date: String,
    val description: String,
    val tags: String,
    var latitude: Double,
    var longitude: Double,
    val imageUrl: String? = null

) : Parcelable {
    fun toJson(): String = JSONObject().apply {
        put("name", name)
        put("date", date)
        put("description", description)
        put("tags", tags)
        put("latitude", latitude)
        put("longitude", longitude)
    }.toString()

    companion object {
        fun fromJson(json: String): Trip {
            val jsonObject = JSONObject(json)
            return Trip(
                name = jsonObject.optString("name", "Unknown Name"),
                date = jsonObject.optString("date", "No Date"),
                description = jsonObject.optString("description", "No Description"),
                tags = jsonObject.optString("tags", "No Tags"),
                latitude = jsonObject.optDouble("latitude", 0.0),
                longitude = jsonObject.optDouble("longitude", 0.0)
            )
        }

        fun fromJsonArray(jsonArray: String): List<Trip> {
            val list = mutableListOf<Trip>()
            val array = JSONArray(jsonArray)
            for (i in 0 until array.length()) {
                val item = array.get(i)
                val jsonObject = if (item is String) JSONObject(item) else item as JSONObject // Check if it's a String or JSONObject
                list.add(fromJson(jsonObject.toString()))
            }
            return list
        }



        fun toJsonArray(tripList: List<Trip>): String {
            val jsonArray = JSONArray()
            for (trip in tripList) {
                jsonArray.put(JSONObject(trip.toJson())) // Correctly serialize each trip to JSON
            }
            return jsonArray.toString()
        }

    }
}







