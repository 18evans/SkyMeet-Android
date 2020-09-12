package evans18.skymeet.data

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import com.pusher.client.channel.SubscriptionEventListener
import evans18.skymeet.BuildConfig
import evans18.skymeet.data.database.FlightDao
import evans18.skymeet.data.model.entities.Flight
import evans18.skymeet.data.model.remote.FlightIdWithPositionResponse
import evans18.skymeet.data.remote.FlightsApiService
import evans18.skymeet.data.remote.PusherManager
import evans18.skymeet.ui.Resource
import evans18.skymeet.util.GenericsTypeConverter
import evans18.skymeet.util.TAG
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlightManager @Inject constructor(
    val flightsApiService: FlightsApiService,
    val flightDao: FlightDao,
    pusherManager: PusherManager
) {
    val cachedLatestFlights: MediatorLiveData<Resource<List<FlightIdWithPositionResponse>>> = MediatorLiveData()
    //    val cachedLastTrackedFlight: MediatorLiveData<Flight> = MediatorLiveData()
    val cachedPersistedFlights: MediatorLiveData<Resource<List<Flight>>> = MediatorLiveData()

    init {
        cachedPersistedFlights.value = Resource.Success(Single.fromCallable { flightDao.getAll() }
            .subscribeOn(Schedulers.io())
            .blockingGet()
        )

        pusherManager.bindFlightsLocationEventListener(SubscriptionEventListener { event ->
            Log.d(TAG, "Event ${BuildConfig.PUSHER_CHANNEL_FLIGHTS_EVENT_NEW_LOCATIONS} received data:\n\t\t\t ${event.data}")

            val listFlights = GenericsTypeConverter.toAnyObject<List<FlightIdWithPositionResponse>>(event.data)
            cachedLatestFlights.postValue(Resource.Success(listFlights))
        })
    }

}
