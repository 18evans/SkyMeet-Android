package evans18.skymeet.data.remote

import evans18.skymeet.data.model.entities.Flight
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FlightsApiService {


    companion object {
        /*
        const val PREFIX_FLIGHTS_NEAR = "flightstatus/rest/v2/json/flightsNear"
        const val PREFIX_AIRCRAFT = "aircraft/rest/v1/json/aircraft"
            */
        const val PREFIX_PERSONAL_API_FLIGHT_NEAR = "flightsNear"
        const val PREFIX_PERSONAL_API_FLIGHT = "flights"
    }
    /*

    @GET("$PREFIX_FLIGHTS_NEAR/{lat}/{lon}/{miles}/")
    fun getFlightsNearLocation(
        @Path("lat") lat: Double,
        @Path("lon") long: Double,
        @Path("miles") miles: Int,
        @Query("maxFlights") maxFlights: Int = 1
    ): Flowable<FlightsNearPointResponse>

    @POST("$PREFIX_AIRCRAFT/{id}")
    fun getAircraftById(
        @Path("id") flightId: String,
        @Body emptyString: Array<String> = emptyArray()
    ): Single<AircraftResponse>
    */

    @GET("$PREFIX_PERSONAL_API_FLIGHT_NEAR/{lat}/{lon}/{kmRadius}/")
    fun getFlightsNearLocation(
        @Path("lat") lat: Double,
        @Path("lon") long: Double,
        @Path("kmRadius") km: Int
    ): Single<List<Flight>>

    // December Demo
    @GET("$PREFIX_PERSONAL_API_FLIGHT_NEAR/{lat}/{lon}/{kmRadius}/west/")
    fun getWestFlight(
        @Path("lat") lat: Double,
        @Path("lon") long: Double,
        @Path("kmRadius") km: Int
    ): Single<List<Flight>>

    @GET("$PREFIX_PERSONAL_API_FLIGHT_NEAR/{lat}/{lon}/{kmRadius}/north/")
    fun getNorthFlights(
        @Path("lat") lat: Double,
        @Path("lon") long: Double,
        @Path("kmRadius") km: Int
    ): Single<List<Flight>>

    @GET("$PREFIX_PERSONAL_API_FLIGHT")
    fun getFlightByAircraftId(@Query("aircraftId") id: Int): Single<Flight>

}
