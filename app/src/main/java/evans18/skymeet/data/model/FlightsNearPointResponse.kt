package evans18.skymeet.data.model

import com.google.gson.annotations.SerializedName
import evans18.skymeet.data.model.entities.Flight

@FlightStatsModel
@Deprecated("Use localhost models instead.")
data class FlightsNearPointResponse
constructor(

    @SerializedName("request")
    val request: FlightsNearPointRequest,
    @SerializedName("appendix")
    val appendix: Any,
    @SerializedName("flightPositions")
    val flights: List<Flight>
)