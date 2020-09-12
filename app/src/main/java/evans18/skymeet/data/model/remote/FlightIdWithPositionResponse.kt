package evans18.skymeet.data.model.remote

import evans18.skymeet.data.model.FlightPosition

data class FlightIdWithPositionResponse constructor(
    val flightPosition: FlightPosition,
    val idAircraft: Int
)