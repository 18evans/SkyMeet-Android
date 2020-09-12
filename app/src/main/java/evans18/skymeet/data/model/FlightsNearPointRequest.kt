package evans18.skymeet.data.model

import com.google.gson.annotations.SerializedName

@FlightStatsModel
@Deprecated("Use localhost models instead.")
data class FlightsNearPointRequest constructor(
    @SerializedName("latitude")
    val latitude: Any,
    @SerializedName("longitude")
    val longitude: Any,
    @SerializedName("radiusMiles")
    val radiusMiles: Any,
    @SerializedName("maxFlights")
    val maxFlights: Any,
    @SerializedName("extendedOptions")
    val extendedOptions: Any,
    @SerializedName("url")
    val url: String
)