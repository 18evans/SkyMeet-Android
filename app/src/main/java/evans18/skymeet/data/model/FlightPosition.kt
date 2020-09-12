package evans18.skymeet.data.model

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

//Collects various properties of a Flight at a specific moment (see #dateTime)
@LocalhostModel
data class FlightPosition constructor(

    //localhost model
    @Embedded
    @SerializedName("location")
    val location: Location

) {
    data class Location constructor(
        @SerializedName("latitude")
        val latitude: Double,
        @SerializedName("longitude")
        val longitude: Double
    )

}

//flightStats model
/*
@SerializedName("lon")
val longitude: Double,
@SerializedName("lat")
val latitude: Double,
//speed miles per hour
@SerializedName("speedMph")
val speedMph: Int,
//altitude in Feet
@SerializedName("altitudeFt")
val altitudeFt: Int,
//date and time that this positions was recorded at. Includes up to seconds with timezone offset.
@SerializedName("date")
val dateTime: String
) {

//todo make UnitTest
val speedKm: Int
    get() = (speedMph * 1.60934).roundToInt()
val altitudeMetres: Int
    get() = (altitudeFt * 0.3048).roundToInt()

@SuppressLint("SimpleDateFormat")
fun getDateAsEpochSeconds(): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            .toEpochSecond(ZoneOffset.UTC)
    } else {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }
        val millis = format.parse(dateTime)!!.time
        millis / 1000 //truncate to seconds
    }
}
}*/